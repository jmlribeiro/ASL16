This folder contains the result of processing the memaslap logs pertaining to the experiment in section 3 of Milestone 2.

avg_tps_{nr. servers}_{get OR set OR total}__{write prop.}.csv corresponds to a list containing, for each replication factor, average aggregated throughput over the repetitions.

avg_std_{nr. servers}_{get OR set OR total}__{write prop.}.csv corresponds to a list containing, for each replication factor, an estimate of the standard deviation of the aggregated throughput over the measured period (30s-90s) over the repetitions.

avg_rep_std_{nr. servers}_{get OR set OR total}__{write prop.}.csv corresponds to a list containing, for each replication factor, the standard deviation of the throughput obtained in each repetition.

avg_rt_{nr. servers}_{get OR set OR total}__{write prop.}.csv corresponds to a list containing, for each replication factor, the average response time.

avg_rt_std_{nr. servers}_{get OR set OR total}_{write prop.}.csv corresponds to a list containing, for each replication factor, the standard deviation of response time.

rev_bucket_{get OR set}_{50p OR 90p OR 99p}_{nr. servers}_{write prop.}.csv corresponds to a list containing, for each replication factor, bucket approximations for percentiles of memaslap response time.