package com.sytoss.traineeplatform.repositories;

import com.sytoss.traineeplatform.bom.PersonalExam;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalExamConnector extends MongoRepository<PersonalExam, Long> {

}

//    @Modifying
//    @Query("UPDATE ShaftDTO T SET T.cabinPosition= :cabinPosition WHERE T.id=:id")
//    void updateCabinPositionById(
//            @Param(value = "cabinPosition") Integer cabinPosition,
//            @Param(value = "id") Long id);
//
//    @Modifying
//    @Query("UPDATE ShaftDTO T SET T.doorState= :doorState WHERE T.id=:id")
//    void updateDoorStateById(
//            @Param(value = "doorState") DoorState doorState,
//            @Param(value = "id") Long id);
//
//    @Modifying
//    @Query("UPDATE ShaftDTO T SET T.engineState= :engineState WHERE T.id=:id")
//    void updateEngineStateById(
//            @Param(value = "engineState") EngineState engineState,
//            @Param(value = "id") Long id);
//
//    @Modifying
//    @Query("UPDATE ShaftDTO T SET T.sequenceOfStops= :sequenceOfStops WHERE T.id=:id")
//    void updateSequenceOfStopsById(
//            @Param(value = "sequenceOfStops") String sequenceOfStops,
//            @Param(value = "id") Long id);
//
//    List<ShaftDTO> findByHouseId(HouseDTO houseDTO);
