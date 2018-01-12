This folder contains zipped files pertaining to the re-run of the nr. servers/rep. factor/write prop. experiment of Milestone 2 in Section 2 of Milestone 3.

memaslap_logs.zip contains the memaslap logs. The logs are of the form log_{client}_{nr. servers}_{rep. factor}_{repetition}_{write prop.}.

mw_logs_{nr. servers} contains the middleware logs for experiments with {nr. servers} servers. Logs are of the form log_{nr. servers}_{rep. factor}_{repetition}_{write prop.}.log.

proc_memaslap.zip contains processed data from the memaslap logs:

avg_tps_{nr. servers}_{get OR set OR total}__{write prop.}.csv corresponds to a list containing, for each replication factor, average aggregated throughput over the repetitions.

avg_std_{nr. servers}_{get OR set OR total}__{write prop.}.csv corresponds to a list containing, for each replication factor, an estimate of the standard deviation of the aggregated throughput over the measured period (30s-90s) over the repetitions.

avg_rep_std_{nr. servers}_{get OR set OR total}__{write prop.}.csv corresponds to a list containing, for each replication factor, the standard deviation of the throughput obtained in each repetition.

avg_rt_{nr. servers}_{get OR set OR total}__{write prop.}.csv corresponds to a list containing, for each replication factor, the average response time.

avg_rt_std_{nr. servers}_{get OR set OR total}_{write prop.}.csv corresponds to a list containing, for each replication factor, the standard deviation of response time.


proc_mw.zip contains processed data from the middleware logs:

rev_t{queue OR server OR total}_{50 OR 90 OR 99}p_{nr. servers}_{request type}_{write prop.}.csv corresponds to a list containing, for replication factor, the 50th/90th/99th percentiles of T_queue/T_server/T_total over the repetitions.

rev_t{queue OR server OR total}_avg_{nr. servers}_{request type}_{write prop.}.csv corresponds to a list containing, for each number of clients, the average of T_queue/T_server/T_total over the repetitions.

rev_t{queue OR server OR total}_std_{nr. servers}_{request type}_{write prop.}.csv corresponds to a list containing, for replication factor, the standard deviation of T_queue/T_server/T_total over the repetitions.