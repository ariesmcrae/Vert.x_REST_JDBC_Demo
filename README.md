# [Vert.x](http://vertx.io) REST service
####This is a Vert.x demo app that provides a REST service and returns JSON. It uses JDBC to query a DB2 database and does transformation with the resulting data.

### Configure gradlew.sh (or gradlew.bat)
* Optimize jvm in `JAVA_OPTS`.
* Change `JAVA_HOME` if you don't have `JAVA_HOME` in your environment path.


### conf.json
* Configure jdbc.
* Tune connection pooling.
* Change webserver host and port.


### src/main/resources/app.js
* Tune the number of Actors (i.e. Verticles e.g. Server.java and Mapper.java).
* Tune number of worker threads (e.g. jdbc-persistor).


### Change logging level
`src/main/resources/platform_lib/logging.properties`


### Add nginx load balancer
Add multiple instances here `/etc/nginx/nginx.conf`

Start nginx `/usr/sbin/nginx`


###How to build
`./gradlew.sh build`

or

`gradlew.bat build`


###How to run
`./gradlew.sh runMod -i`

or

`gradlew.bat runMod -i`


###How to access
`http://[host]:[port]/employees/[employeeNumber]`



###Performance
* 1,000 concurrent users.
* Avg 12ms response time.
* 240 transactions per second.

![100 concurrent users](https://github.com/ariesmcrae/Vert.x_REST_JDBC_Demo/blob/master/EmployeeService/perf/100_users.png)

![1000 concurrent users](https://github.com/ariesmcrae/Vert.x_REST_JDBC_Demo/blob/master/EmployeeService/perf/1000_users.png)

## VM Profile

###OS Version
Red Hat Enterprise Linux Server release 6.5 (Santiago)

###OS Kernel
<pre>
2.6.32-431.el6.x86_64

Linux wxuiib0201 2.6.32-431.el6.x86_64 #1 SMP Sun Nov 10 22:19:54 EST 2013 x86_64 x86_64 x86_64 GNU/Linux

/usr/bin/file: ELF 64-bit LSB executable, x86-64, version 1 (SYSV), dynamically linked (uses shared libs), for GNU/Linux 2.6.18, stripped
</pre>

###CPU (16 cores)
<pre>
processor	: 0
vendor_id	: GenuineIntel
cpu family	: 6
model		: 45
model name	: Intel(R) Xeon(R) CPU E5-2670 0 @ 2.60GHz
stepping	: 2
cpu MHz		: 2600.000
cache size	: 20480 KB
fpu		: yes
fpu_exception	: yes
cpuid level	: 13
wp		: yes
flags		: fpu vme de pse tsc msr pae mce cx8 apic sep mtrr pge mca cmov pat pse36 clflush dts mmx fxsr sse sse2 ss syscall nx rdtscp lm constant_tsc arch_perfmon pebs bts xtopology tsc_reliable nonstop_tsc aperfmperf unfair_spinlock pni pclmulqdq ssse3 cx16 pcid sse4_1 sse4_2 x2apic popcnt aes xsave avx hypervisor lahf_lm ida arat epb xsaveopt pln pts dts
bogomips	: 5200.00
clflush size	: 64
cache_alignment	: 64
address sizes	: 40 bits physical, 48 bits virtual
power management:

processor	: 1
vendor_id	: GenuineIntel
cpu family	: 6
model		: 45
model name	: Intel(R) Xeon(R) CPU E5-2670 0 @ 2.60GHz
stepping	: 2
cpu MHz		: 2600.000
cache size	: 20480 KB
fpu		: yes
fpu_exception	: yes
cpuid level	: 13
wp		: yes
flags		: fpu vme de pse tsc msr pae mce cx8 apic sep mtrr pge mca cmov pat pse36 clflush dts mmx fxsr sse sse2 ss syscall nx rdtscp lm constant_tsc arch_perfmon pebs bts xtopology tsc_reliable nonstop_tsc aperfmperf unfair_spinlock pni pclmulqdq ssse3 cx16 pcid sse4_1 sse4_2 x2apic popcnt aes xsave avx hypervisor lahf_lm ida arat epb xsaveopt pln pts dts
bogomips	: 5200.00
clflush size	: 64
cache_alignment	: 64
address sizes	: 40 bits physical, 48 bits virtual
power management:
</pre>

###Memory
<pre>
MemTotal:       10130088 kB
MemFree:          614100 kB
Buffers:          237720 kB
Cached:          8408792 kB
SwapCached:           92 kB
Active:          4194976 kB
Inactive:        4649176 kB
Active(anon):      93932 kB
Inactive(anon):   103924 kB
Active(file):    4101044 kB
Inactive(file):  4545252 kB
Unevictable:           0 kB
Mlocked:               0 kB
SwapTotal:       8388600 kB
SwapFree:        8388104 kB
Dirty:             91572 kB
Writeback:             0 kB
AnonPages:        197492 kB
Mapped:            18280 kB
Shmem:               244 kB
Slab:             560196 kB
SReclaimable:     525220 kB
SUnreclaim:        34976 kB
KernelStack:        2080 kB
PageTables:        15484 kB
NFS_Unstable:          0 kB
Bounce:                0 kB
WritebackTmp:          0 kB
CommitLimit:    13453644 kB
Committed_AS:     523844 kB
VmallocTotal:   34359738367 kB
VmallocUsed:      304760 kB
VmallocChunk:   34359429752 kB
HardwareCorrupted:     0 kB
AnonHugePages:     96256 kB
HugePages_Total:       0
HugePages_Free:        0
HugePages_Rsvd:        0
HugePages_Surp:        0
Hugepagesize:       2048 kB
DirectMap4k:       10240 kB
DirectMap2M:    10475520 kB
</pre>