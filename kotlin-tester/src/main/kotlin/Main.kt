import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.FileWriter

fun createJobsTable(
    method: String,
    bound: String,
    numsOfJobs: List<Int>,
    repeat: Int
): List<List<List<Any>>> = when (method) {
    "coro" -> when (bound) {
        "cpu" ->
            (0..<repeat).map {
                numsOfJobs.map { numOfJob ->
                    (0..<numOfJob).map {
                        CoroutineTester.cpuBoundJob()
                    }
                }
            }

        "io" ->
            (0..<repeat).map {
                numsOfJobs.map { numOfJob ->
                    (0..<numOfJob).map {
                        CoroutineTester.ioBoundJob()
                    }
                }
            }

        else -> emptyList()
    }

    "thread" -> when (bound) {
        "cpu" ->
            (0..<repeat).map {
                numsOfJobs.map { numOfJob ->
                    (0..<numOfJob).map {
                        { ThreadTester.cpuBoundJob() }
                    }
                }
            }

        "io" ->
            (0..<repeat).map {
                numsOfJobs.map { numOfJob ->
                    (0..<numOfJob).map {
                        { ThreadTester.ioBoundJob() }
                    }
                }
            }

        else -> emptyList()
    }

    else -> emptyList()
}

fun measureElapsedTimes(
    method: String,
    jobsTable: List<List<List<Any>>>
) = when (method) {

    "coro" -> runBlocking {
        (jobsTable as List<List<List<Job>>>).map { jobsRow ->
            jobsRow.map { jobs ->
                CoroutineTester.measureElapsedTime(jobs)
            }
        }
    }

    "thread" ->
        (jobsTable as List<List<List<() -> Unit>>>).map { jobsRow ->
            jobsRow.map { jobs ->
                ThreadTester.measureElapsedTime(jobs)
            }
        }

    else -> emptyList()
}

fun reportElapsedTimes(method: String, bound: String, numsOfJobs: List<Int>, data: List<List<Long>>) {

    File("reports/elapsed-times_${method}-${bound}.csv").writer().use { writer ->
        writer.buffered().use { bufferedWriter ->

            bufferedWriter.write(numsOfJobs.joinToString(","))
            bufferedWriter.write("\n")

            data.forEach { row ->
                bufferedWriter.write(row.joinToString(","))
                bufferedWriter.write("\n")
            }
        }
    }
}

fun main(args: Array<String>) {
    print("Method: ")
    val methods = readlnOrNull()?.split(" ") ?: emptyList()

    print("Bound: ")
    val bounds = readlnOrNull()?.split(" ") ?: emptyList()

    print("The number of jobs: ")
    val numsOfJobs = readlnOrNull()?.let { input ->
        input.split(" ").map { split ->
            split.toInt()
        }
    } ?: emptyList()

    print("Repeat: ")
    val repeat = readlnOrNull()?.toInt() ?: 0

    methods.forEach { method ->
        bounds.forEach { bound ->
            val jobsTable = createJobsTable(method, bound, numsOfJobs, repeat)
            val data = measureElapsedTimes(method, jobsTable)
            reportElapsedTimes(method, bound, numsOfJobs, data)
        }
    }
}
