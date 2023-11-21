import coro
import thread
from itertools import product


def create_jobs_table(method: str, bound: str, nums_of_jobs: list[int], repeat: int):
    if method == 'coro':
        if bound == 'cpu':
            return [[[coro.cpu_bound_job() for _ in range(num_of_jobs)]
                     for num_of_jobs in nums_of_jobs]
                    for _ in range(repeat)]
        elif bound == 'io':
            return [[[coro.io_bound_job() for _ in range(num_of_jobs)]
                     for num_of_jobs in nums_of_jobs]
                    for _ in range(repeat)]
    elif method == 'thread':
        if bound == 'cpu':
            return [[[thread.cpu_bound_job for _ in range(num_of_jobs)]
                     for num_of_jobs in nums_of_jobs]
                    for _ in range(repeat)]
        elif bound == 'io':
            return [[[thread.io_bound_job for _ in range(num_of_jobs)]
                     for num_of_jobs in nums_of_jobs]
                    for _ in range(repeat)]


def measure_elapsed_times(method: str, jobs_table: list[list]):
    if method == 'coro':
        return [[coro.measure_elapsed_time(jobs)
                 for jobs in jobs_row]
                for jobs_row in jobs_table]
    elif method == 'thread':
        return [[thread.measure_elapsed_time(jobs)
                 for jobs in jobs_row]
                for jobs_row in jobs_table]


def report_elapsed_times(method: str, bound: str, nums_of_jobs: list[int], data: list[list[int]]):
    with open(f'reports/elapsed-times_{method}-{bound}.csv', 'w') as file:
        file.write(','.join(map(str, nums_of_jobs)))
        file.write('\n')

        for row in data:
            file.write(','.join(map(str, row)))
            file.write('\n')


def main():
    methods = list(input('Method: ').split())
    bounds = list(input('Bound: ').split())
    nums_of_jobs = list(map(int, input('The number of jobs: ').split()))
    repeat = int(input('Repeat: '))

    for method, bound in product(methods, bounds):
        jobs_table = create_jobs_table(method, bound, nums_of_jobs, repeat)
        data = measure_elapsed_times(method, jobs_table)
        report_elapsed_times(method, bound, nums_of_jobs, data)


if __name__ == '__main__':
    main()
