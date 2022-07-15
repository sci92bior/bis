package com.htt.bis.facade.exercise

import com.htt.bis.annotation.AdminAuthorization
import com.htt.bis.annotation.UserAuthorization
import com.htt.bis.common.containsIgnoreCaseOrNull
import com.htt.bis.domain.ExplosiveUnit
import com.htt.bis.domain.core.SimpleEntity
import com.htt.bis.domain.course.Topic
import com.htt.bis.domain.exercise.DatabaseItemQuantity
import com.htt.bis.domain.exercise.Exercise
import com.htt.bis.domain.exercise.QExercise
import com.htt.bis.dto.course.TopicDto
import com.htt.bis.dto.exercise.CreateExerciseRequest
import com.htt.bis.dto.exercise.DatabaseItemQuantityDto
import com.htt.bis.dto.exercise.ExerciseDTO
import com.htt.bis.dto.exercise.ExerciseFilterDTO
import com.htt.bis.exception.ExerciseCreationException
import com.htt.bis.facade.ifFound
import com.htt.bis.service.AuthenticationService
import com.htt.bis.service.database.*
import com.querydsl.core.types.dsl.BooleanExpression
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Component
class ExerciseFacade(
    private val simpleEntityService: SimpleEntityService,
    private val explosiveUnitService: ExplosiveUnitService,
    private val exerciseService: ExerciseService,
    private val topicService: TopicService,
    private val databaseItemQuantityService: DatabaseItemQuantityService,
    private val authenticationService: AuthenticationService
) {
    var logger: Logger = LoggerFactory.getLogger(ExerciseFacade::class.java)

    @UserAuthorization
    fun getAllExercises(exerciseFilterDTO: ExerciseFilterDTO, pageNo: Int, pageSize: Int, sort: List<String>?): Page<ExerciseDTO> {
        logger.info("Getting page $pageNo from Exercises")
        val query = prepareFilterQuery(exerciseFilterDTO)
        return exerciseService.getAll(query, pageNo, pageSize, sort).map {
            mapExerciseToExerciseDto(it)
        }
    }

    @UserAuthorization
    fun getExerciseById(id: Long): ExerciseDTO {
        logger.info("Getting Exercise with id $id")
        exerciseService.getById(id).ifFound {
            return mapExerciseToExerciseDto(it)
        }
    }


    @UserAuthorization
    fun createExercise(createExerciseRequest: CreateExerciseRequest): Long {
        logger.info("Creating Exercise with name ${createExerciseRequest.name}")
        val currentUser = authenticationService.getCurrentLoggedUserId()
        var topic: Topic? = null
        if (createExerciseRequest.topicId != null) {
            topicService.getById(createExerciseRequest.topicId!!).ifFound {
                topic = it
            }
        }

        val exerciseToSave = Exercise(
            topic = topic,
            name = createExerciseRequest.name,
            startDate = LocalDateTime.parse(createExerciseRequest.startDate.replace("Z", "")),
            endDate = LocalDateTime.parse(createExerciseRequest.endDate.replace("Z", "")),
            createdBy = currentUser.toString(),
            creationDate = LocalDateTime.now(),
        )
        val savedExercise = exerciseService.add(exerciseToSave)

        if (createExerciseRequest.itemQuantities != null) {
            createExerciseRequest.itemQuantities!!.forEach {
                var simpleEntity: SimpleEntity? = null
                if (it.explosiveUnitId != null) {
                    throw ExerciseCreationException("You should provider simpleEntity or explosiveUnit")
                }
                if (it.simpleEntityId != null) {
                    simpleEntityService.getById(it.simpleEntityId!!).ifFound { findSimpleEntity ->
                        simpleEntity = findSimpleEntity
                    }
                }

                databaseItemQuantityService.add(
                    DatabaseItemQuantity(
                        simpleEntity = simpleEntity,
                        quantity = it.quantity,
                        exercise = savedExercise
                    )
                )
            }
        }

        if (createExerciseRequest.explosiveUnits != null) {
            createExerciseRequest.explosiveUnits!!.forEach { explosiveUnitQuantity ->
                var explosiveUnit: ExplosiveUnit? = null

                if (explosiveUnitQuantity.explosiveUnitId != null) {
                    explosiveUnitService.getById(explosiveUnitQuantity.explosiveUnitId!!).ifFound { findExplosiveUnit ->
                        explosiveUnit = findExplosiveUnit
                        databaseItemQuantityService.add(
                            DatabaseItemQuantity(
                                explosiveUnit = explosiveUnit,
                                quantity = explosiveUnitQuantity.quantity,
                                exercise = savedExercise
                            )
                        )
                    }
                } else {
                    throw ExerciseCreationException("You should provider explosiveUnit")
                }

                explosiveUnit!!.explosiveMaterials.forEach { item ->
                    if (databaseItemQuantityService.getByExerciseAndExplosiveMaterial(savedExercise, item.explosiveMaterial)!!.isNotEmpty()) {
                        val explosiveMaterialItemQuantity = databaseItemQuantityService.getByExerciseAndExplosiveMaterial(savedExercise, item.explosiveMaterial)!!.first()
                        val explosiveMaterialItemQuantityToSave =
                            explosiveMaterialItemQuantity.copy(quantity = explosiveMaterialItemQuantity.quantity + (item.quantity * explosiveUnitQuantity.quantity))
                        databaseItemQuantityService.update(explosiveMaterialItemQuantityToSave)
                    } else {
                        databaseItemQuantityService.add(
                            DatabaseItemQuantity(
                                explosiveMaterial = item.explosiveMaterial,
                                quantity = (item.quantity * explosiveUnitQuantity.quantity),
                                exercise = savedExercise
                            )
                        )
                    }
                }
            }
        }
        return savedExercise.id!!
    }


    @AdminAuthorization
    fun deleteExercise(id: Long) {
        logger.info("Deleting Exercise with id $id")
        exerciseService.delete(id)
        logger.info("Exercise with $id deleted")
    }

    fun getExercisesById(ids: Array<String>): List<ExerciseDTO> {
        val exercises = mutableListOf<ExerciseDTO>()
        ids.forEach { id ->
            val exercise = getExerciseById(id.toLong())
            exercises.add(exercise)
        }
        return exercises
    }

    private fun mapExerciseToExerciseDto(exercise: Exercise): ExerciseDTO {
        val databaseItemQuantities = mutableListOf<DatabaseItemQuantityDto>()
        exercise.databaseItems.forEach {
            var name = ""
            var unitType = ""
            if (it.simpleEntity != null) {
                name = it.simpleEntity.name
                unitType = it.simpleEntity.unitType.name
            } else if (it.explosiveUnit != null) {
                name = it.explosiveUnit.name
                unitType = "szt."
            } else {
                name = it.explosiveMaterial!!.name
                unitType = it.explosiveMaterial.unitType.name
            }

            val item = DatabaseItemQuantityDto(
                name = name,
                unit = unitType,
                simpleEntityId = it.simpleEntity?.id,
                explosiveMaterialId = it.explosiveMaterial?.id,
                explosiveUnitId = it.explosiveUnit?.id,
                quantity = it.quantity
            )
            databaseItemQuantities.add(item)
        }
        var topic: TopicDto? = null
        if (exercise.topic != null) {
            topic = TopicDto(id = exercise.topic.id, name = exercise.topic.name)
        }

        return ExerciseDTO(
            id = exercise.id,
            name = exercise.name,
            topic = topic,
            startDate = exercise.startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
            endDate = exercise.endDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
            createdBy = exercise.createdBy,
            databaseItems = databaseItemQuantities,
        )
    }

    private fun prepareFilterQuery(filter: ExerciseFilterDTO): BooleanExpression {
        val qExercise: QExercise = QExercise.exercise

        return qExercise.id.isNotNull
            .and(qExercise.name.containsIgnoreCaseOrNull(filter.q))

    }
}