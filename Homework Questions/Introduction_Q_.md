# Introduction Question

[TOC]

## General Probability Questions

## Networking Questions

### Q1) What is meant by the term **statistical multiplexing**

```
...
```

### Q2) Consider two hosts, A and B, connected by a single link of rate R bps. Suppose that the two hosts are separated by m meters, and suppose the propagation speed along the link is s meters/sec. Host A is to send a packet of size L bits to Host B. 

#### (a) Express the propagation delay, dprop in terms of m and s.

```
...
```

#### (b) Determine the transmission time of the packet, dtrans in terms of L and R.

```
...
```

#### (c) Ignoring the processing and queuing delays, obtain an expression for the end-to-end delay.

```
...
```

#### (d) Suppose Host A begins to transmit the packet at time t=0. At time t=dtrans,where is the last bit of the packet?

```
...
```

#### (e) Suppose dprop is greater than dtrans. At time t= dtrans, where is the first bit of the packet?

```
...
```

#### (f) Suppose dprop is less than dtrans. At time t= dtrans, where is the first bit of the packet?

```
...
```

### Q3) It takes a single bit ten times longer to propagate over a 10Mb/s link than over a 100Mb/s link.True or False?

```
...
```

### Q4) Suppose users share a 1Mbps link. Also suppose each user requires 100 kbps when transmitting, but each user transmits only 10 percent of the time.

```
...
```

#### (a) When circuit switching is used, how many users can be supported?

```
...
```

#### (b) Suppose packet switching is used for the rest of the problem. Find the probability that a given user is transmitting.

```
...
```

#### (c) Suppose there are 40 users. Find the probability that at any given time, exactly n users are transmitting simultaneously. (Note: You should simply express this as an expression rather than computing the exact probability value)

```
...
```

### Q5) Suppose there is exactly one packet switch between a sending host and the receiving host. Assume that the transmission speed of the links between the sending host and the switch and the switch and the receiving host are R1 and R2 respectively. Assuming that the switch uses store-and-forward packet switching, what is the total end-to-end delay to send a packet of length L? Ignore queuing, propagation and processing delays.

```
...
```

### Q6) Review the car-caravan analogy in Section 1.4 of the textbook. Assume a propagation speed of 100 km/hr. 

```
...
```

#### (a) Suppose the caravan travels 200km, beginning in front of one tollbooth, passing through a second tollbooth and finishing just before a third tollbooth. What is the end-to-end delay?(b) Repeat (a), now assuming that there are seven cars in the caravan instead of 10.

> Note: The car­caravan analogy can be used to understand transmission and propagation delays. However, it cannot be used to determine the location of the bits inside a transmission medium, say a wire, unless you make an additional assumption. That’s why the lecture notes use a different method to explain transmission and propagation delays. If you want to use the car­caravan analogy to determine the location of the bits, you will need to make an additional assumption that the cars are stretchable. I will let you think about it.

```
...
```

### Q7) Consider sending a large file of F bits from Host A to Host B. There are two links (and one router) between A and B, and the links are uncongested (that is, no queuing delays). Host A segments the file into segments of S bits each and adds 40 bits of header to each segment, forming packets of L= 40 +S bits. Each link has a transmission rate of R bps. Find the value of S that minimizes the delay of moving the file from Host to Host B. Disregard propagation delay. 

```
...
```

### Q8) n this problem we consider sending real-time voice from Host A to Host B over a packet-switched network (VoIP). Host A converts analog voice to a digital 64kbps bit stream on the fly. Host A then groups the bits into 48-byte packets. There is one link between Host A and B; its transmission rate is 1 Mbps and its propagation delay is 2msec. As soon as Host A gathers a packet, it sends it to Host B. As soon as Host B receives an entire packet, it converts the packet’s bits to an analog signal. How much time elapses from the time a bit is created (from the original analog signal at Host A) until the bit is decoded (as part of the analog signal at Host B)? 

```
...
```

### Q9) Suppose Alice and Bob are sending packets to each other over a computer network. Suppose Trudy positions herself in the network so that she can capture all packets sent by Alice and send whatever she wants to Bob; she can also capture all packets sent by Bob and send whatever she wants to Alice. List some of the malicious things Trudy can do from this position. 

```
...
```

### Q10) Consider the queuing delay in a router buffer (preceding an outbound link). Suppose all packets are L bits, the transmission rate is R bps and that N packets simultaneously arrive at the buffer every LN/R seconds. Find the average queuing delay of a packet. You can assume that the buffer is empty before the arrival of the first batch of N packet. 

```
...
```
