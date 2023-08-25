package com.sytoss.producer.services;

import com.sytoss.domain.bom.exceptions.business.*;
import com.sytoss.domain.bom.exceptions.business.notfound.ExamNotFoundException;
import com.sytoss.domain.bom.exceptions.business.notfound.PersonalExamNotFoundException;
import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.personalexam.*;
import com.sytoss.producer.connectors.ImageConnector;
import com.sytoss.producer.connectors.MetadataConnector;
import com.sytoss.producer.connectors.PersonalExamConnector;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class PersonalExamService extends AbstractService {

    private final MetadataConnector metadataConnector;

    private final PersonalExamConnector personalExamConnector;

    private final ImageConnector imageConnector;

    public PersonalExam create(ExamConfiguration examConfiguration) {
        PersonalExam personalExam = new PersonalExam();
        //TODO: yevgeyv: fix it personalExam.setDiscipline(getDiscipline(examConfiguration.getExam().get);
        Date relevantFrom = examConfiguration.getExam().getRelevantFrom();
        Date relevantTo = examConfiguration.getExam().getRelevantTo();
        personalExam.setName(examConfiguration.getExam().getName());
        personalExam.setExamId(examConfiguration.getExam().getId());
        personalExam.setAssignedDate(new Date());
        personalExam.setRelevantFrom(relevantFrom);
        personalExam.setRelevantTo(relevantTo);
        personalExam.setDiscipline(examConfiguration.getExam().getDiscipline());
        personalExam.setTeacher(examConfiguration.getExam().getTeacher());
        personalExam.setTime((int) TimeUnit.MILLISECONDS.toSeconds(relevantTo.getTime() - relevantFrom.getTime()));
        personalExam.setAmountOfTasks(examConfiguration.getExam().getNumberOfTasks());
        personalExam.setMaxGrade(examConfiguration.getExam().getMaxGrade());
        List<Answer> answers = generateAnswers(examConfiguration.getExam().getNumberOfTasks(), examConfiguration.getExam().getTasks());
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

    private List<Answer> generateAnswers(int numberOfTasks, List<Task> tasks) {
        List<Answer> answers = new ArrayList<>();
        for (int i = 0; i <= numberOfTasks - 1; i++) {
            Random random = new Random();
            int numTask = random.nextInt(tasks.size());
            Task task = tasks.get(numTask);
            Answer answer = new Answer();
            answer.setId((long) (i + 1));
            answer.setStatus(AnswerStatus.NOT_STARTED);
            answer.setTask(task);
            answers.add(answer);
            tasks.remove(task);
        }
        return answers;
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
        examModel.setTime(personalExam.getTime());
        examModel.setAmountOfTasks(personalExam.getAmountOfTasks());
        firstTask.setExam(examModel);
        TaskModel taskModel = new TaskModel();
        taskModel.setQuestionNumber(1);
        firstTask.setTask(taskModel);
        return firstTask;
    }

    public PersonalExam getById(String personalExamId) {
        return personalExamConnector.getById(personalExamId);
    }

    public List<PersonalExam> getAllByExamId(Long examId) {
        return personalExamConnector.getAllByExamId(examId);
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
                    answer.getTeacherGrade().setValue(answerToChange.getTeacherGrade().getValue());
                }
        );

        personalExam.review();
        return personalExamConnector.save(personalExam);
    }

    public byte[] getQuestionImage(String personalExamId) {
        PersonalExam personalExam = personalExamConnector.getById(personalExamId);

        if (ObjectUtils.isEmpty(personalExam)) {
            log.warn("Personal exam with id: [{}] was not found", personalExamId);
            throw new PersonalExamNotFoundException(personalExamId);
        }

        Answer answer = personalExam.getCurrentAnswer();
        return convertToImage(answer.getTask().getQuestion());
    }

    private byte[] convertToImage(String question) {
        BufferedImage tempImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = tempImage.createGraphics();

        Font font = new Font("Arial", Font.BOLD, 20);
        graphics2D.setFont(font);
        FontMetrics fontMetrics = graphics2D.getFontMetrics();

        int width = fontMetrics.stringWidth(question) + 30;
        int height = fontMetrics.getHeight();

        graphics2D.dispose();

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = image.getGraphics();

        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, width, height);
        graphics.setColor(Color.BLACK);
        graphics.setFont(font);
        graphics.drawString(question, 10, fontMetrics.getAscent());
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
        List<PersonalExam> personalExamList = personalExamConnector.getAllByExamId(examConfiguration.getExam().getId());

        if (ObjectUtils.isEmpty(personalExamList)) {
            throw new ExamNotFoundException(examConfiguration.getExam().getId());
        }

        personalExamList.forEach(personalExam -> {
            personalExam.setRelevantFrom(examConfiguration.getExam().getRelevantFrom());
            personalExam.setRelevantTo(examConfiguration.getExam().getRelevantTo());
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
            }
        }
    }
}
