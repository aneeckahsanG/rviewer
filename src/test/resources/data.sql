DELETE FROM dispenser;
DELETE FROM dispenser_spending_line;
insert  into `dispenser`(`id`,`flow_volume`,`status`,`rate`) values ('81964bb5-c922-49c9-b912-cbcf454a9e49',0.5,'CLOSE',12.25),('81964bb5-c922-49c9-b912-cbcf454a9e99',0.5,'OPEN',12.25);
insert  into `dispenser_spending_line`(`id`,`closed_at`,`flow_volume`,`opened_at`,`total_spent`,`dispenser_id`) values (100,'2022-01-01 02:17:00',0.5,'2022-01-01 02:15:00',60.00,'81964bb5-c922-49c9-b912-cbcf454a9e49'),(101,'2022-01-01 02:22:00',0.5,'2022-01-01 02:17:00',150.00,'81964bb5-c922-49c9-b912-cbcf454a9e49'),(301,NULL,0.5,'2022-01-01 02:17:00',150.00,'81964bb5-c922-49c9-b912-cbcf454a9e99');
