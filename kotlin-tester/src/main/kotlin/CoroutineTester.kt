import kotlinx.coroutines.*
import kotlin.math.round
import kotlin.reflect.KCallable
import kotlin.system.measureNanoTime

object CoroutineTester {

    fun cpuBoundJob() = CoroutineScope(
        Dispatchers.Unconfined
    ).async(start = CoroutineStart.LAZY) {
        var result = 0L
        for (i in 0L..<COUNT) {
            result += i
        }
        return@async result
    }

    fun ioBoundJob() = CoroutineScope(
        Dispatchers.Unconfined
    ).async(start = CoroutineStart.LAZY) {
        delay(DELAY)
    }

    suspend fun measureElapsedTime(jobs: List<Job>) = runBlocking {

        val elapsedTime = measureNanoTime {

            // Start jobs
            jobs.forEach {
                it.start()
            }
            // Block until all jobs are done
            jobs.joinAll()
        }
        // Return elapsed time
        println("${jobs.count()} jobs are done (${round(elapsedTime / 10000000.0) / 100}s)")
        return@runBlocking elapsedTime
    }
}