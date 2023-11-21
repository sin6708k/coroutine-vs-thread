import time
from multiprocessing import Process
from const import COUNT, DELAY


def cpu_bound_job():
    result = 0
    for i in range(COUNT):
        result += i
    return result


def io_bound_job():
    time.sleep(DELAY)


def measure_elapsed_time(jobs: list):
    # Create tasks
    tasks = [Process(target=job) for job in jobs]

    # Start tasks
    start_time = time.perf_counter_ns()
    for task in tasks:
        task.start()

    # Block until all tasks are done
    for task in tasks:
        task.join()
    end_time = time.perf_counter_ns()

    # Return elapsed time
    elapsed_time = end_time - start_time
    print(f'{len(jobs)} jobs are done'
          f'({round(elapsed_time / 10 ** 9, 2)}s)')
    return elapsed_time
