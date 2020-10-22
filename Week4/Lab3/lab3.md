# Lab Exercise 3: DNS & Socket Programming

## Exercise 3: Digging into DNS(marked, include in the lab report)

### Question 1. What is the IP address of www.eecs.berkeley.edu . What type of DNS query is sent to get this answer?

```bash
$ dig www.eecs.berkeley.edu 

; <<>> DiG 9.9.5-9+deb8u19-Debian <<>> www.eecs.berkeley.edu
;; global options: +cmd
;; Got answer:
;; ->>HEADER<<- opcode: QUERY, status: NOERROR, id: 22776
;; flags: qr rd ra; QUERY: 1, ANSWER: 3, AUTHORITY: 4, ADDITIONAL: 8

;; OPT PSEUDOSECTION:
; EDNS: version: 0, flags:; udp: 4096
;; QUESTION SECTION:
;www.eecs.berkeley.edu.         IN      A

;; ANSWER SECTION:
www.eecs.berkeley.edu.  40207   IN      CNAME   live-eecs.pantheonsite.io.
live-eecs.pantheonsite.io. 600  IN      CNAME   fe1.edge.pantheon.io.
fe1.edge.pantheon.io.   300     IN      A       23.185.0.1

;; AUTHORITY SECTION:
edge.pantheon.io.       300     IN      NS      ns-2013.awsdns-59.co.uk.
edge.pantheon.io.       300     IN      NS      ns-1213.awsdns-23.org.
edge.pantheon.io.       300     IN      NS      ns-644.awsdns-16.net.
edge.pantheon.io.       300     IN      NS      ns-233.awsdns-29.com.

;; ADDITIONAL SECTION:
ns-233.awsdns-29.com.   26512   IN      A       205.251.192.233
ns-644.awsdns-16.net.   22202   IN      A       205.251.194.132
ns-644.awsdns-16.net.   21560   IN      AAAA    2600:9000:5302:8400::1
ns-1213.awsdns-23.org.  23808   IN      A       205.251.196.189
ns-1213.awsdns-23.org.  26918   IN      AAAA    2600:9000:5304:bd00::1
ns-2013.awsdns-59.co.uk. 25164  IN      A       205.251.199.221
ns-2013.awsdns-59.co.uk. 21866  IN      AAAA    2600:9000:5307:dd00::1

;; Query time: 18 msec
;; SERVER: 129.94.242.2#53(129.94.242.2)
;; WHEN: Sat Oct 10 05:24:02 AEDT 2020
;; MSG SIZE  rcvd: 425
```

* The IP address is `23.185.0.1`
* `Type A` 


### Question 2. What is the canonical name for the eecs.berkeley web server? Suggest a reason for having an alias for this server.

* THe canonical name is `fe1.edge.pantheon.io.` and `live-eecs.pantheonsite.io.`
* It is helpful for user to access multiple services.

### Question 3. What can you make of the rest of the response (i.e. the details available in the Authority and Additional sections)?

* Authority section:  Authoritative DNS name server of this query
* Additional section: show the IP address about name server in authority section

### Question 4. What is the IP address of the local nameserver for your machine?

* machine IP address: `129.94.242.2`, it is recorded in the bottom of the dig output.

### Question 5. What are the DNS nameservers for the “eecs.berkeley.edu.” domain (note: the domain name is eecs.berkeley.edu and not www.eecs.berkeley.edu . This is an example of what is referred to as the apex/naked domain)? Find out their IP addresses? What type of DNS query is sent to obtain this information?

```bash
$ dig eecs.berkeley.edu -t NS

; <<>> DiG 9.9.5-9+deb8u19-Debian <<>> eecs.berkeley.edu -t NS
;; global options: +cmd
;; Got answer:
;; ->>HEADER<<- opcode: QUERY, status: NOERROR, id: 27862
;; flags: qr rd ra; QUERY: 1, ANSWER: 5, AUTHORITY: 0, ADDITIONAL: 9

;; OPT PSEUDOSECTION:
; EDNS: version: 0, flags:; udp: 4096
;; QUESTION SECTION:
;eecs.berkeley.edu.             IN      NS

;; ANSWER SECTION:
eecs.berkeley.edu.      25136   IN      NS      ns.CS.berkeley.edu.
eecs.berkeley.edu.      25136   IN      NS      adns2.berkeley.edu.
eecs.berkeley.edu.      25136   IN      NS      adns3.berkeley.edu.
eecs.berkeley.edu.      25136   IN      NS      adns1.berkeley.edu.
eecs.berkeley.edu.      25136   IN      NS      ns.eecs.berkeley.edu.

;; ADDITIONAL SECTION:
ns.CS.berkeley.edu.     73049   IN      A       169.229.60.61
ns.eecs.berkeley.edu.   72432   IN      A       169.229.60.153
adns1.berkeley.edu.     3191    IN      A       128.32.136.3
adns1.berkeley.edu.     324     IN      AAAA    2607:f140:ffff:fffe::3
adns2.berkeley.edu.     3191    IN      A       128.32.136.14
adns2.berkeley.edu.     3191    IN      AAAA    2607:f140:ffff:fffe::e
adns3.berkeley.edu.     9664    IN      A       192.107.102.142
adns3.berkeley.edu.     6131    IN      AAAA    2607:f140:a000:d::abc

;; Query time: 0 msec
;; SERVER: 129.94.242.2#53(129.94.242.2)
;; WHEN: Sat Oct 10 15:32:41 AEDT 2020
;; MSG SIZE  rcvd: 307
```
|DNS name server | IPV4 | IPV6|
|----|----|---|
|ns.CS.berkeley.edu.| 169.229.60.61|-|
|ns.eecs.berkeley.edu.|169.229.60.153| - |
|adns1.berkeley.edu.|128.32.136.3|2607:f140:ffff:fffe::3|
|adns2.berkeley.edu.|128.32.136.14|2607:f140:ffff:fffe::e|
|adns3.berkeley.edu.|192.107.102.142|2607:f140:a000:d::abc|

### Question 6. What is the DNS name associated with the IP address 111.68.101.54? What type of DNS query is sent to obtain this information?

```bash
$ dig -x 111.68.101.54     

; <<>> DiG 9.9.5-9+deb8u19-Debian <<>> -x 111.68.101.54
;; global options: +cmd
;; Got answer:
;; ->>HEADER<<- opcode: QUERY, status: NOERROR, id: 28595
;; flags: qr rd ra; QUERY: 1, ANSWER: 1, AUTHORITY: 6, ADDITIONAL: 13

;; OPT PSEUDOSECTION:
; EDNS: version: 0, flags:; udp: 4096
;; QUESTION SECTION:
;54.101.68.111.in-addr.arpa.    IN      PTR

;; ANSWER SECTION:
54.101.68.111.in-addr.arpa. 1720 IN     PTR     webserver.seecs.nust.edu.pk.

;; AUTHORITY SECTION:
in-addr.arpa.           7031    IN      NS      d.in-addr-servers.arpa.
in-addr.arpa.           7031    IN      NS      f.in-addr-servers.arpa.
in-addr.arpa.           7031    IN      NS      e.in-addr-servers.arpa.
in-addr.arpa.           7031    IN      NS      c.in-addr-servers.arpa.
in-addr.arpa.           7031    IN      NS      a.in-addr-servers.arpa.
in-addr.arpa.           7031    IN      NS      b.in-addr-servers.arpa.

;; ADDITIONAL SECTION:
a.in-addr-servers.arpa. 26110   IN      A       199.180.182.53
a.in-addr-servers.arpa. 7031    IN      AAAA    2620:37:e000::53
b.in-addr-servers.arpa. 63686   IN      A       199.253.183.183
b.in-addr-servers.arpa. 7031    IN      AAAA    2001:500:87::87
c.in-addr-servers.arpa. 28103   IN      A       196.216.169.10
c.in-addr-servers.arpa. 7031    IN      AAAA    2001:43f8:110::10
d.in-addr-servers.arpa. 18204   IN      A       200.10.60.53
d.in-addr-servers.arpa. 7031    IN      AAAA    2001:13c7:7010::53
e.in-addr-servers.arpa. 35748   IN      A       203.119.86.101
e.in-addr-servers.arpa. 7031    IN      AAAA    2001:dd8:6::101
f.in-addr-servers.arpa. 18181   IN      A       193.0.9.1
f.in-addr-servers.arpa. 7031    IN      AAAA    2001:67c:e0::1

;; Query time: 0 msec
;; SERVER: 129.94.242.2#53(129.94.242.2)
;; WHEN: Sat Oct 10 15:47:49 AEDT 2020
;; MSG SIZE  rcvd: 472
```

* `webserver.seecs.nust.edu.pk.`, record in ANSWER SECTION
* Type: `PTR`

### Question 7. Run dig and query the CSE nameserver (129.94.242.33) for the mail servers for Yahoo! Mail (again the domain name is yahoo.com, not www.yahoo.com ). Did you get an authoritative answer? Why? (HINT: Just because a response contains information in the authoritative part of the DNS response message does not mean it came from an authoritative name server. You should examine the flags in the response to determine the answer)

```bash
$ dig @129.94.242.33 yahoo.com MX

; <<>> DiG 9.9.5-9+deb8u19-Debian <<>> @129.94.242.33 yahoo.com MX
; (1 server found)
;; global options: +cmd
;; Got answer:
;; ->>HEADER<<- opcode: QUERY, status: NOERROR, id: 56859
;; flags: qr rd ra; QUERY: 1, ANSWER: 3, AUTHORITY: 5, ADDITIONAL: 10

;; OPT PSEUDOSECTION:
; EDNS: version: 0, flags:; udp: 4096
;; QUESTION SECTION:
;yahoo.com.                     IN      MX

;; ANSWER SECTION:
yahoo.com.              130     IN      MX      1 mta6.am0.yahoodns.net.
yahoo.com.              130     IN      MX      1 mta7.am0.yahoodns.net.
yahoo.com.              130     IN      MX      1 mta5.am0.yahoodns.net.

;; AUTHORITY SECTION:
yahoo.com.              70264   IN      NS      ns4.yahoo.com.
yahoo.com.              70264   IN      NS      ns3.yahoo.com.
yahoo.com.              70264   IN      NS      ns2.yahoo.com.
yahoo.com.              70264   IN      NS      ns1.yahoo.com.
yahoo.com.              70264   IN      NS      ns5.yahoo.com.

;; ADDITIONAL SECTION:
ns1.yahoo.com.          237504  IN      A       68.180.131.16
ns1.yahoo.com.          77741   IN      AAAA    2001:4998:130::1001
ns2.yahoo.com.          254931  IN      A       68.142.255.16
ns2.yahoo.com.          83518   IN      AAAA    2001:4998:140::1002
ns3.yahoo.com.          594     IN      A       27.123.42.42
ns3.yahoo.com.          594     IN      AAAA    2406:8600:f03f:1f8::1003
ns4.yahoo.com.          249366  IN      A       98.138.11.157
ns5.yahoo.com.          27594   IN      A       202.165.97.53
ns5.yahoo.com.          43377   IN      AAAA    2406:2000:ff60::53

;; Query time: 1 msec
;; SERVER: 129.94.242.33#53(129.94.242.33)
;; WHEN: Sat Oct 10 15:52:10 AEDT 2020
;; MSG SIZE  rcvd: 399
``` 
* It is not an authoritative answer because in the flag section `;; flags: qr rd ra; QUERY: 1, ANSWER: 3, AUTHORITY: 5, ADDITIONAL: 10`, there is not `aa`.

### Question 8. Repeat the above (i.e. Question 7) but use one of the nameservers obtained in Question 5. What is the result?

```bash
$ dig @ns.CS.berkeley.edu. yahoo.com MX            

; <<>> DiG 9.9.5-9+deb8u19-Debian <<>> @ns.CS.berkeley.edu. yahoo.com MX
; (1 server found)
;; global options: +cmd
;; Got answer:
;; ->>HEADER<<- opcode: QUERY, status: REFUSED, id: 23084
;; flags: qr rd; QUERY: 1, ANSWER: 0, AUTHORITY: 0, ADDITIONAL: 1
;; WARNING: recursion requested but not available

;; OPT PSEUDOSECTION:
; EDNS: version: 0, flags:; udp: 4096
;; QUESTION SECTION:
;yahoo.com.                     IN      MX

;; Query time: 167 msec
;; SERVER: 169.229.60.61#53(169.229.60.61)
;; WHEN: Sat Oct 10 15:59:26 AEDT 2020
;; MSG SIZE  rcvd: 38
```
* It doesn't get any result.

### Question 9. Obtain the authoritative answer for the mail servers for Yahoo! mail. What type of DNS query is sent to obtain this information?

```bash
$ dig @ns1.yahoo.com. yahoo.com MX                  

; <<>> DiG 9.9.5-9+deb8u19-Debian <<>> @ns1.yahoo.com. yahoo.com MX
; (1 server found)
;; global options: +cmd
;; Got answer:
;; ->>HEADER<<- opcode: QUERY, status: NOERROR, id: 5481
;; flags: qr aa rd; QUERY: 1, ANSWER: 3, AUTHORITY: 5, ADDITIONAL: 10
;; WARNING: recursion requested but not available

;; OPT PSEUDOSECTION:
; EDNS: version: 0, flags:; udp: 1272
;; QUESTION SECTION:
;yahoo.com.                     IN      MX

;; ANSWER SECTION:
yahoo.com.              1800    IN      MX      1 mta6.am0.yahoodns.net.
yahoo.com.              1800    IN      MX      1 mta5.am0.yahoodns.net.
yahoo.com.              1800    IN      MX      1 mta7.am0.yahoodns.net.

;; AUTHORITY SECTION:
yahoo.com.              172800  IN      NS      ns2.yahoo.com.
yahoo.com.              172800  IN      NS      ns5.yahoo.com.
yahoo.com.              172800  IN      NS      ns1.yahoo.com.
yahoo.com.              172800  IN      NS      ns3.yahoo.com.
yahoo.com.              172800  IN      NS      ns4.yahoo.com.

;; ADDITIONAL SECTION:
ns1.yahoo.com.          1209600 IN      A       68.180.131.16
ns2.yahoo.com.          1209600 IN      A       68.142.255.16
ns3.yahoo.com.          1800    IN      A       27.123.42.42
ns4.yahoo.com.          1209600 IN      A       98.138.11.157
ns5.yahoo.com.          86400   IN      A       202.165.97.53
ns1.yahoo.com.          86400   IN      AAAA    2001:4998:130::1001
ns2.yahoo.com.          86400   IN      AAAA    2001:4998:140::1002
ns3.yahoo.com.          1800    IN      AAAA    2406:8600:f03f:1f8::1003
ns5.yahoo.com.          86400   IN      AAAA    2406:2000:ff60::53

;; Query time: 145 msec
;; SERVER: 68.180.131.16#53(68.180.131.16)
;; WHEN: Sat Oct 10 16:03:09 AEDT 2020
;; MSG SIZE  rcvd: 399
```
* Query Type : `MX`, record in ANSWER SECTION

### Question 10. In this exercise you simulate the iterative DNS query process to find the IP address of your machine (e.g. lyre00.cse.unsw.edu.au). If you are using VLAB Then find the IP address of one of the following: lyre00.cse.unsw.edu.au, lyre01.cse.unsw.edu.au, drum00.cse.unsw.edu.au or drum01.cse.unsw.edu.au. First, find the name server (query type NS) of the "." domain (root domain). Query this nameserver to find the authoritative name server for the "au." domain. Query this second server to find the authoritative nameserver for the "edu.au." domain. Now query this nameserver to find the authoritative nameserver for "unsw.edu.au". Next query the nameserver of unsw.edu.au to find the authoritative name server of cse.unsw.edu.au. Now query the nameserver of cse.unsw.edu.au to find the IP address of your host. How many DNS servers do you have to query to get the authoritative answer?

* **1. Find the name server of the "."**
```bash
$ dig . NS                        

; <<>> DiG 9.9.5-9+deb8u19-Debian <<>> . NS
;; global options: +cmd
;; Got answer:
;; ->>HEADER<<- opcode: QUERY, status: NOERROR, id: 58829
;; flags: qr rd ra; QUERY: 1, ANSWER: 13, AUTHORITY: 0, ADDITIONAL: 27

;; OPT PSEUDOSECTION:
; EDNS: version: 0, flags:; udp: 4096
;; QUESTION SECTION:
;.                              IN      NS

;; ANSWER SECTION:
.                       68883   IN      NS      a.root-servers.net.
.                       68883   IN      NS      e.root-servers.net.
.                       68883   IN      NS      j.root-servers.net.
.                       68883   IN      NS      d.root-servers.net.
.                       68883   IN      NS      g.root-servers.net.
.                       68883   IN      NS      h.root-servers.net.
.                       68883   IN      NS      b.root-servers.net.
.                       68883   IN      NS      f.root-servers.net.
.                       68883   IN      NS      c.root-servers.net.
.                       68883   IN      NS      m.root-servers.net.
.                       68883   IN      NS      i.root-servers.net.
.                       68883   IN      NS      l.root-servers.net.
.                       68883   IN      NS      k.root-servers.net.

;; ADDITIONAL SECTION:
a.root-servers.net.     245315  IN      A       198.41.0.4
a.root-servers.net.     350114  IN      AAAA    2001:503:ba3e::2:30
b.root-servers.net.     359229  IN      A       199.9.14.201
b.root-servers.net.     80704   IN      AAAA    2001:500:200::b
c.root-servers.net.     195244  IN      A       192.33.4.12
c.root-servers.net.     195244  IN      AAAA    2001:500:2::c
d.root-servers.net.     195244  IN      A       199.7.91.13
d.root-servers.net.     195244  IN      AAAA    2001:500:2d::d
e.root-servers.net.     287123  IN      A       192.203.230.10
e.root-servers.net.     341700  IN      AAAA    2001:500:a8::e
f.root-servers.net.     412862  IN      A       192.5.5.241
f.root-servers.net.     80704   IN      AAAA    2001:500:2f::f
g.root-servers.net.     277629  IN      A       192.112.36.4
g.root-servers.net.     80704   IN      AAAA    2001:500:12::d0d
h.root-servers.net.     287123  IN      A       198.97.190.53
h.root-servers.net.     80703   IN      AAAA    2001:500:1::53
i.root-servers.net.     260882  IN      A       192.36.148.17
i.root-servers.net.     260882  IN      AAAA    2001:7fe::53
j.root-servers.net.     262748  IN      A       192.58.128.30
j.root-servers.net.     80704   IN      AAAA    2001:503:c27::2:30
k.root-servers.net.     262050  IN      A       193.0.14.129
k.root-servers.net.     431650  IN      AAAA    2001:7fd::1
l.root-servers.net.     324492  IN      A       199.7.83.42
l.root-servers.net.     80703   IN      AAAA    2001:500:9f::42
m.root-servers.net.     316714  IN      A       202.12.27.33
m.root-servers.net.     80703   IN      AAAA    2001:dc3::35

;; Query time: 0 msec
;; SERVER: 129.94.242.2#53(129.94.242.2)
;; WHEN: Sat Oct 10 16:08:07 AEDT 2020
;; MSG SIZE  rcvd: 811    
```

**root domain**: `198.41.0.4`

* **2. Find the name server for the au.** : 
```bash
$ dig @198.41.0.4 au. NS          

; <<>> DiG 9.9.5-9+deb8u19-Debian <<>> @198.41.0.4 au. NS
; (1 server found)
;; global options: +cmd
;; Got answer:
;; ->>HEADER<<- opcode: QUERY, status: NOERROR, id: 41866
;; flags: qr rd; QUERY: 1, ANSWER: 0, AUTHORITY: 9, ADDITIONAL: 19
;; WARNING: recursion requested but not available

;; OPT PSEUDOSECTION:
; EDNS: version: 0, flags:; udp: 4096
;; QUESTION SECTION:
;au.                            IN      NS

;; AUTHORITY SECTION:
au.                     172800  IN      NS      m.au.
au.                     172800  IN      NS      d.au.
au.                     172800  IN      NS      q.au.
au.                     172800  IN      NS      t.au.
au.                     172800  IN      NS      s.au.
au.                     172800  IN      NS      r.au.
au.                     172800  IN      NS      n.au.
au.                     172800  IN      NS      a.au.
au.                     172800  IN      NS      c.au.

;; ADDITIONAL SECTION:
m.au.                   172800  IN      A       156.154.100.24
m.au.                   172800  IN      AAAA    2001:502:2eda::24
d.au.                   172800  IN      A       162.159.25.38
d.au.                   172800  IN      AAAA    2400:cb00:2049:1::a29f:1926
q.au.                   172800  IN      A       65.22.196.1
q.au.                   172800  IN      AAAA    2a01:8840:be::1
t.au.                   172800  IN      A       65.22.199.1
t.au.                   172800  IN      AAAA    2a01:8840:c1::1
s.au.                   172800  IN      A       65.22.198.1
s.au.                   172800  IN      AAAA    2a01:8840:c0::1
r.au.                   172800  IN      A       65.22.197.1
r.au.                   172800  IN      AAAA    2a01:8840:bf::1
n.au.                   172800  IN      A       156.154.101.24
n.au.                   172800  IN      AAAA    2001:502:ad09::24
a.au.                   172800  IN      A       58.65.254.73
a.au.                   172800  IN      AAAA    2407:6e00:254:306::73
c.au.                   172800  IN      A       162.159.24.179
c.au.                   172800  IN      AAAA    2400:cb00:2049:1::a29f:18b3

;; Query time: 118 msec
;; SERVER: 198.41.0.4#53(198.41.0.4)
;; WHEN: Sat Oct 10 16:13:18 AEDT 2020
;; MSG SIZE  rcvd: 571
```

IP for "au." : `156.154.100.24`

* **3. find the name server for the "edu.au."** :
```bash
$ dig @156.154.100.24 edu.au. NS

; <<>> DiG 9.9.5-9+deb8u19-Debian <<>> @156.154.100.24 edu.au. NS
; (1 server found)
;; global options: +cmd
;; Got answer:
;; ->>HEADER<<- opcode: QUERY, status: NOERROR, id: 51316
;; flags: qr rd; QUERY: 1, ANSWER: 0, AUTHORITY: 4, ADDITIONAL: 9
;; WARNING: recursion requested but not available

;; OPT PSEUDOSECTION:
; EDNS: version: 0, flags:; udp: 4096
;; QUESTION SECTION:
;edu.au.                                IN      NS

;; AUTHORITY SECTION:
edu.au.                 86400   IN      NS      q.au.
edu.au.                 86400   IN      NS      r.au.
edu.au.                 86400   IN      NS      s.au.
edu.au.                 86400   IN      NS      t.au.

;; ADDITIONAL SECTION:
q.au.                   86400   IN      A       65.22.196.1
r.au.                   86400   IN      A       65.22.197.1
s.au.                   86400   IN      A       65.22.198.1
t.au.                   86400   IN      A       65.22.199.1
q.au.                   86400   IN      AAAA    2a01:8840:be::1
r.au.                   86400   IN      AAAA    2a01:8840:bf::1
s.au.                   86400   IN      AAAA    2a01:8840:c0::1
t.au.                   86400   IN      AAAA    2a01:8840:c1::1

;; Query time: 14 msec
;; SERVER: 156.154.100.24#53(156.154.100.24)
;; WHEN: Sat Oct 10 16:16:05 AEDT 2020
;; MSG SIZE  rcvd: 275
```

IP for "edu.au." : `65.22.196.1`

* **4. Find the name server for "unsw.edu.au"**
```bash
$ dig @65.22.196.1 unsw.edu.au NS         

; <<>> DiG 9.9.5-9+deb8u19-Debian <<>> @65.22.196.1 unsw.edu.au NS
; (1 server found)
;; global options: +cmd
;; Got answer:
;; ->>HEADER<<- opcode: QUERY, status: NOERROR, id: 29758
;; flags: qr rd; QUERY: 1, ANSWER: 0, AUTHORITY: 3, ADDITIONAL: 6
;; WARNING: recursion requested but not available

;; OPT PSEUDOSECTION:
; EDNS: version: 0, flags:; udp: 4096
;; QUESTION SECTION:
;unsw.edu.au.                   IN      NS

;; AUTHORITY SECTION:
unsw.edu.au.            900     IN      NS      ns2.unsw.edu.au.
unsw.edu.au.            900     IN      NS      ns3.unsw.edu.au.
unsw.edu.au.            900     IN      NS      ns1.unsw.edu.au.

;; ADDITIONAL SECTION:
ns1.unsw.edu.au.        900     IN      A       129.94.0.192
ns2.unsw.edu.au.        900     IN      A       129.94.0.193
ns3.unsw.edu.au.        900     IN      A       192.155.82.178
ns1.unsw.edu.au.        900     IN      AAAA    2001:388:c:35::1
ns2.unsw.edu.au.        900     IN      AAAA    2001:388:c:35::2

;; Query time: 24 msec
;; SERVER: 65.22.196.1#53(65.22.196.1)
;; WHEN: Sat Oct 10 16:18:53 AEDT 2020
;; MSG SIZE  rcvd: 198
```

IP for "unsw.edu.au" : `129.94.0.192`

* **5. Find the name server for "cse.unsw.edu.au"**
```bash
$ dig @129.94.0.192 cse.unsw.edu.au NS      

; <<>> DiG 9.9.5-9+deb8u19-Debian <<>> @129.94.0.192 cse.unsw.edu.au NS
; (1 server found)
;; global options: +cmd
;; Got answer:
;; ->>HEADER<<- opcode: QUERY, status: NOERROR, id: 36799
;; flags: qr rd; QUERY: 1, ANSWER: 0, AUTHORITY: 2, ADDITIONAL: 5
;; WARNING: recursion requested but not available

;; OPT PSEUDOSECTION:
; EDNS: version: 0, flags:; udp: 4096
;; QUESTION SECTION:
;cse.unsw.edu.au.               IN      NS

;; AUTHORITY SECTION:
cse.unsw.edu.au.        10800   IN      NS      maestro.orchestra.cse.unsw.edu.au.
cse.unsw.edu.au.        10800   IN      NS      beethoven.orchestra.cse.unsw.edu.au.

;; ADDITIONAL SECTION:
beethoven.orchestra.cse.unsw.edu.au. 10800 IN A 129.94.172.11
beethoven.orchestra.cse.unsw.edu.au. 10800 IN A 129.94.208.3
beethoven.orchestra.cse.unsw.edu.au. 10800 IN A 129.94.242.2
maestro.orchestra.cse.unsw.edu.au. 10800 IN A   129.94.242.33

;; Query time: 4 msec
;; SERVER: 129.94.0.192#53(129.94.0.192)
;; WHEN: Sat Oct 10 16:21:13 AEDT 2020
;; MSG SIZE  rcvd: 164
```

IP for "cse.unsw.edu.au" : `129.94.172.11`

* **6. find the ip address for "lyre00.cse.unsw.edu.au"**
```bash
$ dig @129.94.172.11 lyre00.cse.unsw.edu.au A

; <<>> DiG 9.9.5-9+deb8u19-Debian <<>> @129.94.172.11 lyre00.cse.unsw.edu.au A
; (1 server found)
;; global options: +cmd
;; Got answer:
;; ->>HEADER<<- opcode: QUERY, status: NOERROR, id: 19017
;; flags: qr aa rd ra; QUERY: 1, ANSWER: 1, AUTHORITY: 2, ADDITIONAL: 3

;; OPT PSEUDOSECTION:
; EDNS: version: 0, flags:; udp: 4096
;; QUESTION SECTION:
;lyre00.cse.unsw.edu.au.                IN      A

;; ANSWER SECTION:
lyre00.cse.unsw.edu.au. 3600    IN      A       129.94.210.20

;; AUTHORITY SECTION:
cse.unsw.edu.au.        3600    IN      NS      beethoven.orchestra.cse.unsw.edu.au.
cse.unsw.edu.au.        3600    IN      NS      maestro.orchestra.cse.unsw.edu.au.

;; ADDITIONAL SECTION:
maestro.orchestra.cse.unsw.edu.au. 3600 IN A    129.94.242.33
beethoven.orchestra.cse.unsw.edu.au. 3600 IN A  129.94.242.2

;; Query time: 1 msec
;; SERVER: 129.94.172.11#53(129.94.172.11)
;; WHEN: Sat Oct 10 16:32:03 AEDT 2020
;; MSG SIZE  rcvd: 155
```

* We can get the IP address is `129.94.210.20`
* There are `6` DNS servers to get the authoritative answer.

### Question 11. Can one physical machine have several names and/or IP addresses associated with it?

* Yes
* For a computer, if it has serval NIC(Network interface controller), it could have serval IP address or serval names.