import kotlin.concurrent.thread
import kotlin.math.round
import kotlin.system.measureNanoTime

object ThreadTester {

    fun cpuBoundJob() {
        var result = 0L
        for (i in 0L..<COUNT) {
            result += i
        }
    }

    fun ioBoundJob() {
        Thread.sleep(DELAY)
    }

    fun measureElapsedTime(jobs: List<() -> Unit>): Long {

        // Create tasks
        val tasks = jobs.map {
            thread(start = false, block = it)
        }
        val elapsedTime = measureNanoTime {

            // Start tasks
            tasks.forEach {
                it.start()
            }
            // Block until all tasks are done
            tasks.forEach {
                it.join()
            }
        }
        // Return elapsed time
        println("${jobs.count()} jobs are done (${round(elapsedTime / 10000000.0) / 100}s)")
        return elapsedTime
    }
}