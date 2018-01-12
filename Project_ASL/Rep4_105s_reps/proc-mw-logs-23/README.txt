This folder contains the result of processing the middleware logs pertaining to the experiment in section 3 of Milestone 2.

rev_t{queue OR server OR total}_{50 OR 90 OR 99}p_{nr. servers}_{request type}_{write prop.}.csv corresponds to a list containing, for replication factor, the 50th/90th/99th percentiles of T_queue/T_server/T_total over the repetitions.

rev_t{queue OR server OR total}_avg_{nr. servers}_{request type}_{write prop.}.csv corresponds to a list containing, for each number of clients, the average of T_queue/T_server/T_total over the repetitions.

rev_t{queue OR server OR total}_std_{nr. servers}_{request type}_{write prop.}.csv corresponds to a list containing, for replication factor, the standard deviation of T_queue/T_server/T_total over the repetitions.