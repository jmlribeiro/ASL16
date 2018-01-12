This folder contains the result of processing the memaslap logs pertaining to the fine-grained experiment run in section 1 of Milestone 2.

avg_tps_{nr. threads}.csv corresponds to a list containing, for each number of clients, average aggregated throughput over the 4 repetitions.

avg_std_{nr. threads}.csv corresponds to a list containing, for each number of clients, an estimate of the standard deviation of the aggregated throughput over the measured period (60s-120s) over the 4 repetitions.

avg_rep_std_{nr. threads}.csv corresponds to a list containing, for each number of clients, the standard deviation for average aggregated throughput over the 4 repetitions.

rev_bucket_{50p OR 90p OR 99p}_{num_threads}.csv corresponds to a list containing, for each number of clients, the approximation of the 50th OR 90th OR 99th percentile yielded by the Log2 Dist buckets, average over the 4 repetitions.