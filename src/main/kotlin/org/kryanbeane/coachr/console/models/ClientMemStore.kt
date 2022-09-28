package org.kryanbeane.coachr.console.models

import mu.KotlinLogging
import java.util.*
import kotlin.collections.ArrayList

private val logger = KotlinLogging.logger {}

class ClientMemStore: ClientStore {
    private val clients = ArrayList<ClientModel>()

    /**
     * find all clients in client arraylist
     *
     * @return client list
     */
    override fun findAll(): List<ClientModel> {
        return clients
    }

    /**
     * find client matching name param
     *
     * @param clientName
     * @return client or null
     */
    override fun findClient(clientName: String): ClientModel? {
        return clients.find{
                client -> client.fullName == clientName
        }
    }

    /**
     * find workout matching workoutId param
     *
     * @param clientId
     * @param workoutId
     * @return workout or null
     */
    override fun findWorkout(clientName: String, workoutName: String): WorkoutModel? {
        return findClient(clientName)?.workoutPlan?.find{
                workout -> workout.name == workoutName
        }
    }

    /**
     * find exercise matching exerciseId param
     *
     * @param clientId
     * @param workoutId
     * @param exerciseId
     * @return exercise or null
     */
    override fun findExercise(clientName: String, workoutName: String, exerciseName: String): ExerciseModel? {
        return findWorkout(clientName, workoutName)?.exercises?.find{
            exercise -> exercise.name == exerciseName
        }
    }

    /**
     * create client from client param
     *
     * @param client
     */
    override fun createClient(client: ClientModel) {
        clients.add(client)
        logClients()
    }

    /**
     * create workout from workout param for client from param
     *
     * @param client
     * @param workout
     */
    override fun createClientWorkout(client: ClientModel, workout: WorkoutModel) {
        client.workoutPlan.add(workout)
        logWorkouts(client)
    }

    /**
     * create exercise from param for workout from param
     *
     * @param workout
     * @param exercise
     */
    override fun createExercise(workout: WorkoutModel, exercise: ExerciseModel) {
        workout.exercises.add(exercise)
        logExercises(workout)
    }

    /**
     * update client with client param details
     *
     * @param client
     */
    override fun updateClientDetails(client: ClientModel) {
        val foundClient = findClient(client.fullName)
        if (foundClient != null) {
            foundClient.fullName = client.fullName
            foundClient.phoneNumber = client.phoneNumber
            foundClient.emailAddress = client.emailAddress
        }
    }

    /**
     * update workout with workout param details
     *
     * @param workout
     */
    override fun updateClientWorkout(client: ClientModel, workout: WorkoutModel) {
        val foundWorkout = findWorkout(client.fullName, workout.name)
        if (foundWorkout != null) {
            foundWorkout.name = workout.name
            foundWorkout.type = foundWorkout.type
        }
    }

    /**
     * update exercise in workout from param with exercise param details
     *
     * @param workout
     * @param exercise
     */
    override fun updateExercise(client: ClientModel, workout: WorkoutModel, exercise: ExerciseModel) {
        val foundExercise = findExercise(client.fullName, workout.name, exercise.name)
        if (foundExercise != null) {
            foundExercise.name = exercise.name
            foundExercise.description = exercise.description
            foundExercise.sets = exercise.sets
            foundExercise.reps = exercise.reps
            foundExercise.repsInReserve = exercise.repsInReserve
        }
    }

    /**
     * loop through client arraylist param and log all clients
     *
     * @param clientList
     */
    internal fun logClients() {
        clients.forEach{
            logger.info {
                it.fullName + "\n" +
                "Client ID: " + it.id + "\n" +
                "Email Address: " + it.emailAddress + "\n" +
                "Phone Number: " + it.phoneNumber + "\n" +
                "Number of Workouts in Plan: " + it.workoutPlan.size + "\n" + "\n"
            }
        }
    }

    /**
     * loop through workout plan arraylist of client param and log all workouts
     *
     * @param client
     */
    internal fun logWorkouts(client: ClientModel) {
        client.workoutPlan.forEach{
            logger.info("$it")
        }
    }

    /**
     * loop through exercise arraylist of workout param and log all exercises
     *
     * @param workout
     */
    internal fun logExercises(workout: WorkoutModel) {
        workout.exercises.forEach{
            logger.info("$it")
        }
    }
}