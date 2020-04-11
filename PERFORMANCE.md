# No Providers
## Set 20191022-230434 (24,909,446 records)

**Performance Patches on T450s**

|Threads|Taskset|Time   |Lines/s|System CPU|User CPU|Context    |
|-------|------:|---:   |------:|---------:|-------:|-------:   |
|1      |0-3    |42.9s  |593,000|4.1s      |76.5s   |14903/61114|
|1      |0,2    |39.0s  |638,000|3.5s      |63.5s   |13103/57636|
|2      |0-3    |40.9s  |622.000|3.8s      |76.0s   |15473/61750|
|2      |0,2    |29.6s  |860,300|3.2s      |61.5s   |33008/38287|
|4      |0-3    |37.5s  |673,000|6.2s      |102.0s  |54064/50807|
|4      |0,2    |41.7s  |607,000|5.0s      |62.0s   |31300/51226|
|-      |0-3    |24.3s  |1,038,800|3.8s    |96.4s   |43235/39050|


**XLT 5.0.0 on T450s**
|Threads|Taskset|Time   |Lines/s|System CPU|User CPU|Context    |
|-------|------:|---:   |------:|---------:|-------:|-------:   |
|1      |0-3    |65.5s  |       |3.6s      |80.1s   |7100/112733|
|1      |0,2    |64.7s  |       |3.4s      |72.9s   |19332/104713|
|2      |0-3    |42.2s  |       |3.9s      |93.6s   |15119/107658|
|2      |0,2    |34.7s  |       |3.2s      |72.3s   |51257/62109|
|-      |0-3    |32.5s  |       |4.5s      |111.5s  |56720/55964|


Before ArrayList replacement
1, 0-3, 24,909,446 (45,822 ms) - (553,543 lines/s)
1, 0,2, 24,909,446 (42,758 ms) - (593,082 lines/s)

SimpleArrayList
1, 0-3, 24,909,446 (42,778 ms) - (593,082 lines/s)
1, 0,2, 24,909,446 (41,890 ms) - (607,547 lines/s)

RAMDisk 6GB Torrid without output
1, 0-3, 24,909,446 (36,288 ms) - (691,929 lines/s)
1, 0,2, 24,909,446 (33,530 ms) - (754,831 lines/s)
2, 0-3, 24,909,446 (39,442 ms) - (638,703 lines/s)
2, 0,2, 24,909,446 (27,661 ms) - (922,572 lines/s)
-, 0-3, 24,909,446 (24,370 ms) - (1,037,893 lines/s)
-, 0,2, 24,909,446 (28,315 ms) - (889,623 lines/s)

Single typecode as Character
-, 0-3, 24,909,446 (23,176 ms) - (1,083,019 lines/s)

Array instead of Map for DataRecordFactory to find class
1, 0-3, 24,909,446 (34,527 ms) - (732,630 lines/s)
2, 0-3, 24,909,446 (35,740 ms) - (711,698 lines/s)
2, 0,2, 24,909,446 (27,945 ms) - (922,572 lines/s)
-, 0-3, 24,909,446 (22,989 ms) - (1,132,247 lines/s)

# Merge Rules

No Rules, no providers
24,909,446 (22,489 ms) - (1,132,247 lines/s)

Analytics
24,909,446 (25,513 ms) - (996,377 lines/s)

Skip empty rules
24,909,446 (23,055 ms) - (1,083,019 lines/s)

All Provider and analytics
24,909,446 (53,555 ms) - (469,989 lines/s)

All Provider and No Analytics
24,909,446 (49,837 ms) - (508,356 lines/s)


# Provider and Threading Research
##  20191022-230434 (24,909,446 records) - RAM Disk

Start Data, Default Threading, all providers
-, 0-3, 24,909,446 (49,630 ms) - (508,356 lines/s)

Start Data, Default Threading, NO providers
-, 0-3, 24,909,446 (22,331 ms) - (1,132,247 lines/s)

GeneralReportProvider:      24,909,446 (24,548 ms) - (1,037,893 lines/s)
TransactionsReportProvider: 24,909,446 (23,671 ms) - (1,083,019 lines/s)
ActionsReportProvider:      24,909,446 (23,572 ms) - (1,083,019 lines/s)
All three:                  24,909,446 (29,625 ms) - (858,946 lines/s)
RequestsReportProvider:     24,909,446 (25,480 ms) - (996,377 lines/s)
CustomTimersReportProvider: 24,909,446 (23,449 ms) - (1,083,019 lines/s)
ErrorsReportProvider:       24,909,446 (23,976 ms) - (1,083,019 lines/s)
ResponseCodesReportProvider:24,909,446 (22,536 ms) - (1,132,247 lines/s)

T450s
None - 24,909,446 (22,916 ms) - (1,086,989 lines/s) Context I/V: 45168/59234
All  - 24,909,446 (54,576 ms) - (456,418 lines/s) Context I/V: 237777/496005

Pool with sync on providers
All  - 24,909,446 (51,169 ms) - (486,807 lines/s) Context I/V: 95376/182702

Pool with sync on providers and 10 partitions
All - 24,909,446 (67,272 ms) - (370,280 lines/s) Context I/V: 184052/994996

As before but threadly and no partitions
All - 24,909,446 (47,491 ms) - (524,509 lines/s) Context I/V: 90901/159721

10k chunks
All - 24,909,446 (49,374 ms) - (504,505 lines/s) Context I/V: 80224/47379

No Host, No REquest
24,909,446 (38,856 ms) - (641,071 lines/s) Context I/V: 44955/36344

# Move of expensive to preprocessing
##  20191022-230434 (24,909,446 records) - RAM Disk, T450s

### Before
All providers, no merge rules, 0-3, 24,909,446 (45,236 ms) - (550,655 lines/s)
All providers, no merge rules, 0-3, 24,909,446 (45,608 ms) - (546,164 lines/s)

### After Hostextractor
All providers, no merge rules, 0-3, 24,909,446 (44,495 ms) - (559,826 lines/s)

### After url hash move
All providers, no merge rules, 0-3, 24,909,446 (43,134 ms) - (577,490 lines/s)

Dropped fast parse int idea without checks and using char[] instead of string for less conversion, 
no effect or even slower on T450s and 4+4

### Double as fast version
24,909,446 (43,661 ms) - (570,519 lines/s)


#Idea
* Give a provider only what it needs (only its typecodes)
* Providers announce what Data they want and get that only
* Give each line to all providers in a thread