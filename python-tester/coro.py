import asyncio
import time
from typing import Coroutine
from const import COUNT, DELAY


async def cpu_bound_job():
    result = 0
    for i in range(COUNT):
        result += i
    return result


async def io_bound_job():
    await asyncio.sleep(DELAY)


async def run_jobs(jobs: list[Coroutine]):
    # Create tasks
    tasks = [asyncio.create_task(job) for job in jobs]

    # Block until all tasks are done
    await asyncio.gather(*tasks)


def measure_elapsed_time(jobs: list[Coroutine]):
    # Run jobs
    start_time = time.perf_counter_ns()
    asyncio.run(run_jobs(jobs))
    end_time = time.perf_counter_ns()

    # Return elapsed time
    elapsed_time = end_time - start_time
    print(f'{len(jobs)} jobs are done'
          f'({round(elapsed_time / 10 ** 9, 2)}s)')
    return elapsed_time
