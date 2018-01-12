This folder contains the result of processing the memaslap logs pertaining to the main experiment run in section 2 of Milestone 2 with a set logging ratio of 5.

avg_tps_{nr. servers}_{op type}.csv corresponds to a list containing, for each replication factor, average aggregated throughput over the repetitions.

avg_std_{nr. servers}_{op type}.csv corresponds to a list containing, for each replication factor, an estimate of the standard deviation of the aggregated throughput over the measured period (30s-90s) over the repetitions.

avg_rep_std_{nr. servers}_{op type}.csv corresponds to a list containing, for each replication factor, the standard deviation of the throughput obtained in each repetition.

avg_rt_{nr. servers}_{op type}.csv corresponds to a list containing, for each replication factor, the average response time.

avg_rt_std_{nr. servers}_{op type}.csv corresponds to a list containing, for each replication factor, the standard deviation of response time.

rev_bucket_{request type}_{50p OR 90p OR 99p}_{nr. servers}.csv corresponds to a list containing, for each replication factor, bucket approximations for percentiles of memaslap response time.