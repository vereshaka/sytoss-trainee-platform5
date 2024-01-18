package com.sytoss.producer.services;

import com.sytoss.domain.bom.analytics.AnalyticGrade;
import com.sytoss.domain.bom.analytics.Analytics;
import com.sytoss.domain.bom.exceptions.business.*;
import com.sytoss.domain.bom.exceptions.business.notfound.PersonalExamNotFoundException;
import com.sytoss.domain.bom.lessons.*;
import com.sytoss.domain.bom.personalexam.*;
import com.sytoss.domain.bom.users.Student;
import com.sytoss.producer.connectors.LessonsConnector;
import com.sytoss.producer.connectors.MetadataConnector;
import com.sytoss.producer.connectors.PersonalExamConnector;
import com.sytoss.producer.convertor.PersonalExamConvertor;
import com.sytoss.producer.interfaces.AnswerGenerator;
import com.sytoss.producer.writers.ExcelBuilder;
import com.sytoss.producer.writers.GroupExcelBuilder;
import com.sytoss.producer.bom.ScreenshotModel;
import com.sytoss.producer.connectors.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PersonalExamService extends AbstractService {

    private final MetadataConnector metadataConnector;

    private final PersonalExamConnector personalExamConnector;

    private final LessonsConnector lessonsConnector;

    private final ObjectProvider<ExcelBuilder> excelBuilderFactory;

    private final ObjectProvider<GroupExcelBuilder> groupExcelBuilderFactory;

    private final AnswerGenerator answerGenerator;

    private final UserConnector userConnector;

    private final ScreenshotConnector screenshotConnector;

    @Value("${image-provider.image-folder}")
    private String imageUrl;

    /**
     * Creates or updates personal exam for student and exam assignee based on {@link ExamConfiguration}
     *
     * @param examConfiguration new configuration
     * @return created or updated personal exam
     */
    public PersonalExam createOrUpdate(ExamConfiguration examConfiguration) {
        Optional<PersonalExam> personalExamOpt = personalExamConnector.findByExamAssigneeIdAndStudent_Id(
                examConfiguration.getExamAssignee().getId(), examConfiguration.getStudent().getId());

        PersonalExam personalExam = personalExamOpt.orElse(new PersonalExam());

        Date relevantFrom = examConfiguration.getExamAssignee().getRelevantFrom();
        Date relevantTo = examConfiguration.getExamAssignee().getRelevantTo();
        personalExam.setName(examConfiguration.getExam().getName());
        personalExam.setExamAssigneeId(examConfiguration.getExamAssignee().getId());
        personalExam.setAssignedDate(new Date());
        personalExam.setRelevantFrom(relevantFrom);
        personalExam.setRelevantTo(relevantTo);
        personalExam.setDiscipline(examConfiguration.getExam().getDiscipline());
        personalExam.setTeacher(examConfiguration.getExam().getTeacher());
        personalExam.setTime((int) TimeUnit.MILLISECONDS.toSeconds(relevantTo.getTime() - relevantFrom.getTime()));
        personalExam.setAmountOfTasks(examConfiguration.getExam().getNumberOfTasks());
        personalExam.setMaxGrade(examConfiguration.getExam().getMaxGrade());
        List<Answer> answers = answerGenerator.generateAnswers(examConfiguration.getExam().getNumberOfTasks(), examConfiguration.getExam().getTasks());
        personalExam.setAnswers(answers);
        double sumOfCoef = 0;
        for (Answer answer : answers) {
            sumOfCoef += answer.getTask().getCoef();
        }
        personalExam.setStudent(examConfiguration.getStudent());
        personalExam.setSumOfCoef(sumOfCoef);

        personalExam = personalExamConnector.save(personalExam);
        log.info("Personal exam created. Id: " + personalExam.getId());
        return personalExam;
    }

    public boolean taskDomainIsUsed(Long taskDomainId) {
        int count = personalExamConnector.countByAnswersTaskTaskDomainIdAndStatusNotLike(taskDomainId, PersonalExamStatus.FINISHED);
        return count > 0;
    }

    private Discipline getDiscipline(Long disciplineId) {
        return metadataConnector.getDiscipline(disciplineId);
    }

    public PersonalExam summary(String id) {
        PersonalExam personalExam = personalExamConnector.getById(id);
        personalExam.summary();
        personalExam.setAnswers(personalExam.getAnswers().stream().sorted(Comparator.comparing(a -> a.getTask().getCode(), Comparator.nullsLast(Comparator.naturalOrder()))).toList());
        return personalExam;
    }

    public Question start(String personalExamId) {
        Long studentId = getCurrentUser().getId();
        Date currentDate = new Date();
        PersonalExam personalExam = getById(personalExamId);
        if (!Objects.equals(personalExam.getStudent().getId(), studentId)) {
            throw new StudentDontHaveAccessToPersonalExam(studentId, personalExamId);
        }
        if (personalExam.getAnswers().isEmpty()) {
            throw new PersonalExamHasNoAnswerException();
        }
        if (currentDate.before(personalExam.getRelevantFrom())) {
            throw new PersonalExamStartedDateException();
        }
        Answer answer;
        if (PersonalExamStatus.NOT_STARTED.equals(personalExam.getStatus())) {
            personalExam.start();
            answer = personalExam.getAnswers().get(0);
            answer.inProgress();
        } else if (PersonalExamStatus.IN_PROGRESS.equals(personalExam.getStatus())) {
            answer = personalExam.getAnswers().stream().filter(item -> item.getStatus().equals(AnswerStatus.IN_PROGRESS)).findFirst().orElse(null);
            if (answer == null) {
                //TODO: yevgenyv: not possible from business logic case
                Optional<Answer> notStartedAnswer = personalExam.getAnswers().stream().filter(item -> item.getStatus().equals(AnswerStatus.NOT_STARTED)).findFirst();
                if (notStartedAnswer.isPresent()) {
                    answer = notStartedAnswer.get();
                } else {
                    return null;
                }
            }
        } else {
            throw new PersonalExamIsFinishedException();
        }
        personalExam = personalExamConnector.save(personalExam);
        Question firstTask = new Question();
        ExamModel examModel = new ExamModel();
        examModel.setName(personalExam.getName());
        examModel.setTime((int) TimeUnit.MILLISECONDS.toSeconds(personalExam.getRelevantTo().getTime() - new Date().getTime()));
        examModel.setAmountOfTasks(personalExam.getAmountOfTasks());
        firstTask.setExam(examModel);
        TaskModel taskModel = new TaskModel();
        taskModel.setQuestionNumber(1);
        taskModel.setNeedCheckQuery(answer.getTask().getCheckAnswer() != null);
        firstTask.setTask(taskModel);
        return firstTask;
    }

    public PersonalExam getById(String personalExamId) {
        return personalExamConnector.getById(personalExamId);
    }

    public List<PersonalExam> getAllByExamAssigneeId(Long examAssigneeId) {
        return personalExamConnector.getAllByExamAssigneeId(examAssigneeId);
    }

    public List<PersonalExam> getByStudentId(Long userId) {
        List<PersonalExam> personalExams = personalExamConnector.getAllByStudent_IdOrderByAssignedDateDesc(userId);

        personalExams.forEach(personalExam -> {
            if (personalExam.getStatus().equals(PersonalExamStatus.REVIEWED)) {
                personalExam.summary();
            }
        });

        return personalExams;
    }

    public List<PersonalExam> getByTeacherId(Long userId) {
        List<PersonalExam> personalExams = personalExamConnector.getAllByTeacher_IdOrderByAssignedDateDesc(userId);
        personalExams.forEach(personalExam -> {
            if (personalExam.getStatus().equals(PersonalExamStatus.REVIEWED)) {
                personalExam.summary();
            }
        });
        return personalExams;
    }

    public PersonalExam review(PersonalExam personalExamToChange) {
        PersonalExam personalExam = getById(personalExamToChange.getId());

        if (ObjectUtils.isEmpty(personalExam)) {
            throw new PersonalExamNotFoundException(personalExamToChange.getId());
        }

        personalExamToChange.getAnswers().forEach(
                answerToChange -> {
                    Answer answer = personalExam.getAnswerById(answerToChange.getId());
                    if (answer.getTeacherGrade() == null) {
                        answer.setTeacherGrade(new Grade());
                    }
                    answer.getTeacherGrade().setValue(answerToChange.getTeacherGrade().getValue());
                }
        );
        personalExam.setComment(personalExamToChange.getComment());
        personalExam.review();
        personalExam.summary();
        PersonalExam result = personalExamConnector.save(personalExam);

        PersonalExam personalExam1 = new PersonalExam();
        personalExam1.setId(personalExam.getId());
        personalExam1.setExamAssigneeId(personalExam.getExamAssigneeId());
        personalExam1.finish();
        Analytics analytics = new Analytics();
        analytics.setDiscipline(new Discipline());
        analytics.getDiscipline().setId(personalExam.getDiscipline().getId());
        analytics.setPersonalExam(personalExam1);
        analytics.setExam(new Exam());
        Exam exam = lessonsConnector.getExamByAssignee(personalExam1.getExamAssigneeId());
        analytics.getExam().setId(exam.getId());
        analytics.setStudent(new Student());
        analytics.getStudent().setId(personalExam.getStudent().getId());
        analytics.setGrade(new AnalyticGrade(personalExam.getSummaryGrade(), personalExam.getSpentTime()));
        analytics.setStartDate(personalExam.getStartedDate());

        lessonsConnector.updateAnalytic(analytics);

        return result;
    }

    public byte[] getQuestionImage(String personalExamId) {
        PersonalExam personalExam = personalExamConnector.getById(personalExamId);

        if (ObjectUtils.isEmpty(personalExam)) {
            log.warn("Personal exam with id: [{}] was not found", personalExamId);
            throw new PersonalExamNotFoundException(personalExamId);
        }

        Answer answer = personalExam.getCurrentAnswer();

        List<String> questionParts = Arrays.stream(answer.getTask().getQuestion().split(" ")).toList();
        List<String> unitedQuestionParts = new ArrayList<>();

        StringBuilder questionStringBuilder = new StringBuilder();
        for (String questionPart : questionParts) {
            StringBuilder tmpStringBuilder = new StringBuilder(questionStringBuilder);
            if (questionStringBuilder.isEmpty() || (tmpStringBuilder.append(questionPart).append(" ")).length() < 140) {
                questionStringBuilder.append(questionPart).append(" ");
            } else {
                unitedQuestionParts.add(questionStringBuilder.toString());
                questionStringBuilder = new StringBuilder(questionPart).append(" ");
            }
        }
        unitedQuestionParts.add(questionStringBuilder.toString());
        return convertToImage(unitedQuestionParts);
    }

    private byte[] convertToImage(List<String> questionParts) {
        BufferedImage tempImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = tempImage.createGraphics();

        Font font = new Font("TimesRoman", Font.PLAIN, 20);
        graphics2D.setFont(font);
        FontMetrics fontMetrics = graphics2D.getFontMetrics();

        int width = fontMetrics.stringWidth(longestString(questionParts)) + 30;
        int height = fontMetrics.getHeight() * questionParts.size();

        graphics2D.dispose();

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = image.getGraphics();

        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, width, height);
        graphics.setColor(Color.BLACK);
        graphics.setFont(font);
        for (int i = 0; i < questionParts.size(); i++) {
            graphics.drawString(questionParts.get(i), 10, fontMetrics.getAscent() * (i + 1));
        }
        graphics.dispose();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", baos);
        } catch (IOException e) {
            throw new ConvertToImageException("Error during image creating", e);
        }

        return baos.toByteArray();
    }

    public List<PersonalExam> reschedule(ExamConfiguration examConfiguration) {
        List<PersonalExam> personalExamList = personalExamConnector.getAllByExamAssigneeId(examConfiguration.getExamAssignee().getId());

        personalExamList.forEach(personalExam -> {
            personalExam.setRelevantFrom(examConfiguration.getExamAssignee().getRelevantFrom());
            personalExam.setRelevantTo(examConfiguration.getExamAssignee().getRelevantTo());
            personalExamConnector.save(personalExam);
        });

        return personalExamList;
    }

    @Scheduled(fixedRate = 60 * 1000)
    public void setExpiredTestsAsDone() {
        List<PersonalExam> allPersonalExams = personalExamConnector.findAll();
        Date currentDate = new Date();
        for (PersonalExam personalExam : allPersonalExams) {
            if (personalExam.getRelevantTo().before(currentDate) &&
                    (personalExam.getStatus().equals(PersonalExamStatus.NOT_STARTED)
                            || personalExam.getStatus().equals(PersonalExamStatus.IN_PROGRESS))) {
                personalExam.finish();
                personalExamConnector.save(personalExam);
                log.info("Personal exam with id=" + personalExam.getId() + "is finished");
            }
        }
    }

    private String longestString(List<String> questionParts) {
        String maxString = questionParts.get(0);
        for (String questionPart : questionParts) {
            if (questionPart.length() > maxString.length()) {
                maxString = questionPart;
            }
        }
        return maxString;
    }

    public List<PersonalExam> getByExamAssigneeId(Long examAssigneeId) {
        List<PersonalExam> personalExams = personalExamConnector.getByExamAssigneeId(examAssigneeId);
        personalExams.forEach(PersonalExam::summary);
        return personalExams;
    }

    public List<PersonalExam> deleteByExamAssigneeId(Long examAssigneeId) {
        List<PersonalExam> personalExams = personalExamConnector.getAllByExamAssigneeId(examAssigneeId);
        personalExamConnector.deleteAll(personalExams);
        return personalExams;
    }

    public List<PersonalExam> deleteByExamAssigneeIds(List<Long> assigneeIds) {
        List<PersonalExam> personalExams = new ArrayList<>();
        for (Long assigneeId : assigneeIds) {
            personalExams.addAll(deleteByExamAssigneeId(assigneeId));
        }
        return personalExams;
    }

    public ExamAssigneeAnswersModel reviewByAnswers(ExamAssigneeAnswersModel examAssigneeAnswersModel) {
        for (ReviewGradeModel grade : examAssigneeAnswersModel.getGrades()) {
            PersonalExam personalExam = getById(grade.getPersonalExamId());

            if (ObjectUtils.isEmpty(personalExam)) {
                throw new PersonalExamNotFoundException(grade.getPersonalExamId());
            }

            Answer answer = personalExam.getAnswerById(grade.getAnswer().getId());

            if (grade.getAnswer().getTeacherGrade() == null) {
                grade.getAnswer().setTeacherGrade(new Grade());
            }
            answer.getTeacherGrade().setValue(grade.getAnswer().getTeacherGrade().getValue());
            personalExam.review();
            personalExamConnector.save(personalExam);
            grade.setAnswer(answer);
        }
        return examAssigneeAnswersModel;
    }

    public byte[] getExcelReport(Long examAssigneeId) throws IOException {
        ExcelBuilder excelBuilder = excelBuilderFactory.getObject();
        ExamReportModel examReportModel = lessonsConnector.getReportInfo(examAssigneeId);
        List<PersonalExam> personalExams = personalExamConnector.getAllByExamAssigneeId(examAssigneeId);

        List<TaskReportModel> uniqueTasks = personalExams.stream()
                .flatMap(exam -> exam.getAnswers().stream().map(Answer::getTask))
                .collect(Collectors.toMap(
                        Task::getId,
                        task -> task,
                        (existing, replacement) -> existing
                ))
                .values()
                .stream().map(task -> {
                    TaskReportModel taskReportModel = new TaskReportModel();
                    taskReportModel.setId(task.getId());
                    taskReportModel.setCode(task.getCode());
                    taskReportModel.setQuestion(task.getQuestion());
                    return taskReportModel;
                })
                .toList();

        examReportModel.setTasks(uniqueTasks);
        List<PersonalExamReportModel> personalExamReportModels = convertToReportModel(personalExams);
        Workbook wb = excelBuilder.createExcelReportByExamAssignee(examReportModel, personalExamReportModels);
        excelBuilder.write("D:/tmp.xlsx", wb);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        wb.write(bos);
        byte[] byteArray = bos.toByteArray();
        bos.close();
        return byteArray;
    }

    private List<PersonalExamReportModel> convertToReportModel(List<PersonalExam> personalExams) {
        return personalExams.stream().map(personalExam -> {
            personalExam.summary();

            List<AnswerReportModel> answerReportModels = personalExam.getAnswers().stream().map(answer -> {
                TaskReportModel taskReportModel = new TaskReportModel();
                taskReportModel.setId(answer.getTask().getId());
                taskReportModel.setQuestion(answer.getTask().getQuestion());

                AnswerReportModel answerReportModel = new AnswerReportModel();
                answerReportModel.setAnswerStatus(answer.getStatus());
                answerReportModel.setTeacherGrade(answer.getTeacherGrade());
                answerReportModel.setTask(taskReportModel);
                answerReportModel.setTimeSpent(answer.getTimeSpent());
                answerReportModel.setSystemGrade(answer.getGrade());
                return answerReportModel;
            }).toList();

            PersonalExamReportModel personalExamReportModel = new PersonalExamReportModel();
            personalExamReportModel.setExamName(personalExam.getName());
            personalExamReportModel.setEmail(personalExam.getStudent().getEmail());
            personalExamReportModel.setStudentId(personalExam.getStudent().getId());
            personalExamReportModel.setStudentName(personalExam.getStudent().getLastName() + " " + personalExam.getStudent().getFirstName());
            personalExamReportModel.setSummary(personalExam.getSummaryGrade());
            personalExamReportModel.setSpentTime(personalExam.getSpentTime());
            personalExamReportModel.setAnswers(answerReportModels);
            personalExamReportModel.setGroupName(personalExam.getStudent().getPrimaryGroup().getName());
            personalExamReportModel.setPersonalExamStatus(personalExam.getStatus());
            personalExamReportModel.setStartDate(personalExam.getStartedDate());
            return personalExamReportModel;
        }).toList();
    }

    public byte[] getExcelReportByGroup(Long groupId) throws IOException {
        GroupExcelBuilder groupExcelBuilder = groupExcelBuilderFactory.getObject();
        List<PersonalExam> personalExams = personalExamConnector.getAllByStudent_PrimaryGroup_Id(groupId);
        List<PersonalExamReportModel> reportModels = convertToReportModel(personalExams);
        Workbook wb = groupExcelBuilder.createExcelReportByGroup(reportModels);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        wb.write(bos);
        byte[] byteArray = bos.toByteArray();
        bos.close();
        return byteArray;
    }

    public List<PersonalExam> updateTask(Task task) {
        List<PersonalExam> personalExams = personalExamConnector.getAllByAnswersTaskIdAndStatusIs(task.getId(), PersonalExamStatus.NOT_STARTED);
        for(PersonalExam personalExam : personalExams){
           for(Answer answer : personalExam.getAnswers()){
               if(Objects.equals(answer.getTask().getId(), task.getId())){
                   answer.setTask(task);
               }
           }
        }
        return personalExams;
    }


    public void makeScreenshot(String pictureCode, String personalExamId){
        //pictureCode = pictureCode.substring(16);
        ScreenshotModel screenshotModel = new ScreenshotModel();
        Date currentDate = new Date();

       // Long userId = userConnector.getByUid(getCurrentUser().getUid()).getId();
        Long userId = getCurrentUser().getId();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyddMMhh24mmss");
        String formattedDate = simpleDateFormat.format(currentDate);
        String imageName = personalExamId+"-"+userId+"-"+formattedDate+".jpg";

        byte[] picture = Base64.getMimeDecoder().decode(pictureCode);
        byte[] decodedPicture = Arrays.copyOfRange(picture, 15, picture.length);

        try {
            File imageFile = new File(imageUrl+imageName);
            FileOutputStream fis = new FileOutputStream(imageFile);
            fis.write(decodedPicture);
            fis.flush();
            fis.close();
        } catch (Exception e) {
            throw new ScreenshotCouldNotCreateImageException(e);
        }

        screenshotModel.setPersonalExamId(personalExamId);
        screenshotModel.setUserUid(getCurrentUser().getUid());
        screenshotModel.setUserId(userId);
        screenshotModel.setEmail(getCurrentUser().getEmail());
        screenshotModel.setDate(currentDate);
        screenshotModel.setImageFileName(imageName);

        screenshotConnector.save(screenshotModel);
    }

    public List<PersonalExam> getListOfStudentsPersonalExam(Long disciplineId, List<Long> examAssignees, List<Student> students) {
        List<PersonalExam> personalExams = new ArrayList<>();
        for(Student student : students){
            List<PersonalExam> personalExamsByStudent = personalExamConnector.getAllByStudent_Id(student.getId());
            for(PersonalExam personalExam : personalExamsByStudent){
                if(personalExam.getDiscipline()!=null && Objects.equals(personalExam.getDiscipline().getId(), disciplineId)
                        && examAssignees.contains(personalExam.getExamAssigneeId())){
                    personalExam.summary();
                    personalExams.add(personalExam);
                }
            }
        }
        return personalExams;
    }

    public List<PersonalExam> getStudentPersonalExams() {
        return getByStudentId(getCurrentUser().getId());
    }
}
