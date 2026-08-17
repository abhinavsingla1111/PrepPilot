# Computer Networks — 100 Interview MCQs

> Format: Each question has a difficulty tag `[Easy] / [Medium] / [Hard]`, options (or True/False, or one-word), the correct answer, and a one-line justification.

---

## Section A — Easy (Q1–Q35)

**Q1. [Easy]** How many layers are in the OSI model?

- A) 4
- B) 5
- C) 7
- D) 8

**Answer:** C — The OSI model has 7 layers.

**Q2. [Easy] (One word)** The layer responsible for routing and logical addressing is the **\_\_** layer.

**Answer:** Network — Layer 3 handles routing and IP addressing.

**Q3. [Easy]** Which protocol is connection-oriented and reliable?

- A) UDP
- B) TCP
- C) IP
- D) ICMP

**Answer:** B — TCP provides reliable, ordered delivery.

**Q4. [Easy]** Which protocol is connectionless?

- A) TCP
- B) UDP
- C) HTTP
- D) FTP

**Answer:** B — UDP is connectionless and unreliable.

**Q5. [Easy] (True/False)** IP operates at the Network layer.

**Answer:** True — IP is a Layer 3 protocol.

**Q6. [Easy]** The default port for HTTP is:

- A) 21
- B) 80
- C) 443
- D) 25

**Answer:** B — HTTP uses port 80.

**Q7. [Easy]** The default port for HTTPS is:

- A) 80
- B) 443
- C) 8080
- D) 22

**Answer:** B — HTTPS uses port 443.

**Q8. [Easy] (One word)** The device that connects different networks and routes packets is a **\_\_**.

**Answer:** Router — Routers forward packets between networks.

**Q9. [Easy]** A MAC address operates at which layer?

- A) Network
- B) Data Link
- C) Transport
- D) Physical

**Answer:** B — MAC addresses are Layer 2 (Data Link).

**Q10. [Easy]** How many bits is an IPv4 address?

- A) 16
- B) 32
- C) 64
- D) 128

**Answer:** B — IPv4 addresses are 32 bits.

**Q11. [Easy]** How many bits is an IPv6 address?

- A) 32
- B) 64
- C) 128
- D) 256

**Answer:** C — IPv6 addresses are 128 bits.

**Q12. [Easy] (True/False)** DNS translates domain names to IP addresses.

**Answer:** True — DNS resolves names to IPs.

**Q13. [Easy]** DNS commonly uses which port?

- A) 53
- B) 80
- C) 25
- D) 110

**Answer:** A — DNS uses port 53.

**Q14. [Easy]** Which device operates at the Data Link layer to forward frames by MAC?

- A) Hub
- B) Switch
- C) Router
- D) Repeater

**Answer:** B — A switch forwards frames using MAC addresses.

**Q15. [Easy] (One word)** The protocol that assigns IP addresses dynamically is **\_\_**.

**Answer:** DHCP — DHCP dynamically assigns IP configuration.

**Q16. [Easy]** Which port does SSH use?

- A) 22
- B) 23
- C) 21
- D) 25

**Answer:** A — SSH uses port 22.

**Q17. [Easy]** Which layer does HTTP belong to (OSI)?

- A) Transport
- B) Application
- C) Network
- D) Session

**Answer:** B — HTTP is an Application-layer protocol.

**Q18. [Easy] (True/False)** A hub broadcasts data to all ports.

**Answer:** True — Hubs are dumb devices that broadcast to all ports.

**Q19. [Easy]** The loopback IPv4 address is:

- A) 0.0.0.0
- B) 127.0.0.1
- C) 192.168.1.1
- D) 255.255.255.255

**Answer:** B — 127.0.0.1 is the loopback address.

**Q20. [Easy]** Which protocol is used to send email?

- A) POP3
- B) SMTP
- C) IMAP
- D) HTTP

**Answer:** B — SMTP sends/relays email.

**Q21. [Easy] (One word)** The unit of data at the Network layer is a **\_\_**.

**Answer:** Packet — Layer 3 PDU is the packet.

**Q22. [Easy]** The unit of data at the Data Link layer is a:

- A) Packet
- B) Frame
- C) Segment
- D) Bit

**Answer:** B — Layer 2 PDU is the frame.

**Q23. [Easy]** The unit of data at the Transport layer (TCP) is a:

- A) Frame
- B) Segment
- C) Packet
- D) Datagram

**Answer:** B — TCP PDU is a segment (UDP uses datagram).

**Q24. [Easy] (True/False)** TCP uses a three-way handshake to establish a connection.

**Answer:** True — SYN, SYN-ACK, ACK.

**Q25. [Easy]** Which class was 192.168.x.x traditionally part of (private range)?

- A) Class A
- B) Class B
- C) Class C
- D) Class D

**Answer:** C — 192.168.0.0/16 is a Class C private range.

**Q26. [Easy]** Which protocol resolves IP to MAC on a LAN?

- A) DNS
- B) ARP
- C) DHCP
- D) ICMP

**Answer:** B — ARP maps IP addresses to MAC addresses.

**Q27. [Easy] (One word)** The command-line tool that uses ICMP echo to test reachability is **\_\_**.

**Answer:** ping — `ping` tests connectivity via ICMP.

**Q28. [Easy]** Which layer ensures end-to-end delivery and flow control?

- A) Network
- B) Transport
- C) Session
- D) Physical

**Answer:** B — The Transport layer manages end-to-end delivery.

**Q29. [Easy]** FTP typically uses which control port?

- A) 20
- B) 21
- C) 22
- D) 23

**Answer:** B — FTP control uses port 21 (data on 20).

**Q30. [Easy] (True/False)** A subnet mask separates network and host portions of an IP.

**Answer:** True — It identifies network vs host bits.

**Q31. [Easy]** Which is a private IP range?

- A) 10.0.0.0/8
- B) 8.8.8.0/24
- C) 172.15.0.0/16
- D) 200.1.1.0/24

**Answer:** A — 10.0.0.0/8 is private (RFC 1918).

**Q32. [Easy]** Which protocol is used for secure web browsing?

- A) HTTP
- B) HTTPS (HTTP over TLS)
- C) FTP
- D) SMTP

**Answer:** B — HTTPS encrypts HTTP with TLS.

**Q33. [Easy] (One word)** The topology where all devices connect to a central node is **\_\_**.

**Answer:** Star — Star topology uses a central hub/switch.

**Q34. [Easy]** Which layer deals with bits and physical transmission?

- A) Physical
- B) Data Link
- C) Network
- D) Transport

**Answer:** A — The Physical layer transmits raw bits.

**Q35. [Easy]** The broadcast address of a network is:

- A) The first address
- B) The last address (all host bits 1)
- C) 0.0.0.0
- D) 127.0.0.1

**Answer:** B — All-ones host portion is the broadcast address.

---

## Section B — Medium (Q36–Q75)

**Q36. [Medium]** The TCP three-way handshake sequence is:

- A) ACK, SYN, SYN-ACK
- B) SYN, SYN-ACK, ACK
- C) SYN, ACK, FIN
- D) FIN, ACK, SYN

**Answer:** B — SYN → SYN-ACK → ACK establishes the connection.

**Q37. [Medium]** TCP connection termination typically uses:

- A) Two-way handshake
- B) Four-way handshake (FIN/ACK both ways)
- C) One packet
- D) Three-way only

**Answer:** B — FIN, ACK, FIN, ACK closes both directions.

**Q38. [Medium] (One word)** The field in the IP header that prevents infinite loops by decrementing at each hop is **\_\_**.

**Answer:** TTL — Time To Live limits hops.

**Q39. [Medium]** What does a subnet mask /24 mean?

- A) 24 host bits
- B) 24 network bits, 8 host bits
- C) 24 subnets
- D) 24 addresses

**Answer:** B — /24 = 255.255.255.0 (24 network bits).

**Q40. [Medium]** How many usable hosts in a /24 network?

- A) 256
- B) 254
- C) 255
- D) 253

**Answer:** B — 256 minus network and broadcast = 254.

**Q41. [Medium]** Which layer does a router primarily operate at?

- A) Layer 2
- B) Layer 3
- C) Layer 4
- D) Layer 7

**Answer:** B — Routers work at the Network layer (Layer 3).

**Q42. [Medium] (True/False)** UDP provides guaranteed, in-order delivery.

**Answer:** False — UDP is best-effort with no delivery guarantees.

**Q43. [Medium]** Which mechanism does TCP use for reliability?

- A) Checksums only
- B) Sequence numbers, ACKs, and retransmission
- C) Broadcasting
- D) TTL

**Answer:** B — ACKs and retransmissions ensure reliable delivery.

**Q44. [Medium]** NAT is primarily used to:

- A) Encrypt traffic
- B) Map private addresses to public addresses
- C) Route within a subnet
- D) Resolve names

**Answer:** B — NAT translates private/public IP addresses.

**Q45. [Medium]** Which protocol is used by `traceroute` (typically on Linux)?

- A) Only TCP
- B) UDP/ICMP with increasing TTL
- C) HTTP
- D) ARP

**Answer:** B — It sends packets with increasing TTL to map hops.

**Q46. [Medium] (One word)** The DNS record that maps a name to an IPv4 address is the **\_\_** record.

**Answer:** A — The A record maps to IPv4 (AAAA for IPv6).

**Q47. [Medium]** Which DNS record maps a name to another name (alias)?

- A) A
- B) CNAME
- C) MX
- D) PTR

**Answer:** B — CNAME creates an alias to another domain.

**Q48. [Medium]** The MX record is used for:

- A) Web servers
- B) Mail servers
- C) Reverse lookup
- D) Aliases

**Answer:** B — MX records direct email to mail servers.

**Q49. [Medium] (True/False)** ICMP is used by `ping` and error reporting, not for data transfer.

**Answer:** True — ICMP handles diagnostics/error messages.

**Q50. [Medium]** Which layer does TLS/SSL primarily operate at (practically)?

- A) Between Transport and Application (Session/Presentation)
- B) Physical
- C) Network
- D) Data Link

**Answer:** A — TLS sits above TCP, securing application data.

**Q51. [Medium]** Congestion control in TCP includes:

- A) Slow start and congestion avoidance
- B) ARP caching
- C) DNS resolution
- D) TTL decrement

**Answer:** A — Slow start/AIMD manage congestion.

**Q52. [Medium] (One word)** The TCP feature that limits how much unacknowledged data can be in flight based on receiver buffer is the **\_\_** window.

**Answer:** Receive — The receive/advertised window enables flow control.

**Q53. [Medium]** What distinguishes flow control from congestion control?

- A) They are identical
- B) Flow control protects the receiver; congestion control protects the network
- C) Flow control protects the network
- D) Neither exists in TCP

**Answer:** B — Flow control = receiver capacity; congestion control = network load.

**Q54. [Medium]** Which HTTP method is idempotent and safe?

- A) POST
- B) GET
- C) PATCH
- D) DELETE (not safe)

**Answer:** B — GET is safe and idempotent (no side effects).

**Q55. [Medium] (True/False)** HTTP is stateless by default.

**Answer:** True — Each request is independent; state needs cookies/sessions.

**Q56. [Medium]** HTTP status 404 means:

- A) Server error
- B) Not Found
- C) Unauthorized
- D) Redirect

**Answer:** B — 404 indicates the resource wasn't found.

**Q57. [Medium]** HTTP status 500 means:

- A) OK
- B) Internal Server Error
- C) Bad Request
- D) Forbidden

**Answer:** B — 500 is a generic server-side error.

**Q58. [Medium] (One word)** The 3xx class of HTTP status codes indicates **\_\_**.

**Answer:** Redirection — 3xx codes signal redirects.

**Q59. [Medium]** Which is TRUE about switches vs routers?

- A) Switches route between networks
- B) Switches forward within a LAN (L2); routers connect networks (L3)
- C) Routers use MAC only
- D) They are identical

**Answer:** B — Switch = intra-LAN L2; router = inter-network L3.

**Q60. [Medium]** A collision domain is reduced by:

- A) Hubs
- B) Switches (each port is its own collision domain)
- C) Repeaters
- D) Coax cable

**Answer:** B — Switch ports separate collision domains.

**Q61. [Medium] (True/False)** A broadcast domain is bounded by a router.

**Answer:** True — Routers do not forward broadcasts by default.

**Q62. [Medium]** VLANs are used to:

- A) Encrypt traffic
- B) Logically segment a network at Layer 2
- C) Route packets
- D) Resolve DNS

**Answer:** B — VLANs create isolated L2 broadcast domains.

**Q63. [Medium]** Which protocol prevents Layer 2 loops?

- A) OSPF
- B) STP (Spanning Tree Protocol)
- C) BGP
- D) ARP

**Answer:** B — STP blocks redundant paths to avoid loops.

**Q64. [Medium] (One word)** The routing protocol that uses link-state and Dijkstra's algorithm is **\_\_**.

**Answer:** OSPF — OSPF is a link-state IGP.

**Q65. [Medium]** BGP is best described as:

- A) An interior gateway protocol
- B) The path-vector protocol routing between autonomous systems (Internet)
- C) A LAN protocol
- D) A transport protocol

**Answer:** B — BGP routes between ASes on the Internet.

**Q66. [Medium]** CIDR notation `10.0.0.0/16` provides how many host addresses (total)?

- A) 256
- B) 65,536
- C) 1,024
- D) 16

**Answer:** B — /16 = 2^16 = 65,536 addresses.

**Q67. [Medium] (True/False)** TCP guarantees ordered delivery using sequence numbers.

**Answer:** True — Sequence numbers reorder and detect loss.

**Q68. [Medium]** Which uses UDP for low latency?

- A) File transfer (FTP)
- B) Video streaming / DNS / VoIP
- C) Email
- D) Web page load only

**Answer:** B — Latency-sensitive apps favor UDP.

**Q69. [Medium]** The purpose of a default gateway is to:

- A) Resolve names
- B) Forward traffic destined outside the local subnet
- C) Assign IPs
- D) Encrypt data

**Answer:** B — It routes off-subnet traffic.

**Q70. [Medium] (One word)** The 32-bit or 128-bit value identifying a network interface is an **\_\_** address.

**Answer:** IP — IP addresses identify interfaces.

**Q71. [Medium]** Which is TRUE about symmetric vs asymmetric encryption?

- A) Symmetric uses one shared key; asymmetric uses public/private key pairs
- B) They are identical
- C) Asymmetric uses one key
- D) Symmetric is always slower

**Answer:** A — Symmetric shares a key; asymmetric uses key pairs.

**Q72. [Medium]** TLS handshake uses asymmetric crypto to:

- A) Encrypt all data
- B) Securely exchange/establish a symmetric session key
- C) Resolve DNS
- D) Compress data

**Answer:** B — It negotiates a symmetric key for bulk encryption.

**Q73. [Medium] (True/False)** HTTP/2 supports multiplexing multiple streams over one connection.

**Answer:** True — HTTP/2 multiplexes to avoid head-of-line blocking at the app layer.

**Q74. [Medium]** Which transport does HTTP/3 use?

- A) TCP
- B) QUIC (over UDP)
- C) SCTP
- D) ICMP

**Answer:** B — HTTP/3 runs over QUIC/UDP.

**Q75. [Medium]** The purpose of a checksum in TCP/IP headers is to:

- A) Encrypt data
- B) Detect errors/corruption
- C) Compress data
- D) Route packets

**Answer:** B — Checksums detect transmission errors.

---

## Section C — Hard (Q76–Q100)

**Q76. [Hard]** In the TCP state machine, TIME_WAIT exists to:

- A) Speed up closing
- B) Ensure the final ACK is delivered and old duplicate segments expire
- C) Open a new connection
- D) Retransmit SYN

**Answer:** B — It handles delayed segments and reliable close (typically 2\*MSL).

**Q77. [Hard]** Head-of-line blocking in HTTP/2 can still occur at which layer?

- A) Application
- B) TCP (transport) due to in-order delivery
- C) Physical
- D) None

**Answer:** B — TCP's ordering causes HOL blocking that QUIC/HTTP/3 addresses.

**Q78. [Hard] (True/False)** QUIC eliminates transport-layer head-of-line blocking by using independent streams over UDP.

**Answer:** True — Stream loss doesn't block others in QUIC.

**Q79. [Hard]** Nagle's algorithm is used to:

- A) Increase packet count
- B) Reduce many small packets by buffering until ACK/enough data
- C) Encrypt traffic
- D) Resolve DNS

**Answer:** B — It coalesces small writes to improve efficiency.

**Q80. [Hard]** Which option disables Nagle's algorithm for low latency?

- A) SO_REUSEADDR
- B) TCP_NODELAY
- C) SO_KEEPALIVE
- D) TCP_CORK

**Answer:** B — `TCP_NODELAY` sends small segments immediately.

**Q81. [Hard] (One word)** The AIMD in TCP congestion control stands for Additive Increase Multiplicative **\_\_**.

**Answer:** Decrease — Additive Increase / Multiplicative Decrease.

**Q82. [Hard]** How does a subnet /26 divide a /24?

- A) Into 2 subnets
- B) Into 4 subnets of 64 addresses each
- C) Into 8 subnets
- D) Into 16 subnets

**Answer:** B — 2 extra bits → 4 subnets of 64 addresses.

**Q83. [Hard]** How many usable hosts in a /30 subnet?

- A) 4
- B) 2
- C) 1
- D) 0

**Answer:** B — /30 has 4 addresses; 2 usable (common for point-to-point links).

**Q84. [Hard] (True/False)** ARP operates within a single broadcast domain (local subnet).

**Answer:** True — ARP broadcasts don't cross routers.

**Q85. [Hard]** What is a SYN flood attack?

- A) Sending large files
- B) Sending many SYNs without completing handshakes to exhaust server resources
- C) DNS poisoning
- D) MAC spoofing

**Answer:** B — Half-open connections exhaust the backlog queue.

**Q86. [Hard]** SYN cookies mitigate SYN floods by:

- A) Dropping all SYNs
- B) Encoding connection state in the sequence number to avoid storing half-open state
- C) Using UDP
- D) Blocking ports

**Answer:** B — State is reconstructed from the ACK, avoiding memory exhaustion.

**Q87. [Hard] (One word)** The maximum amount of data a TCP segment can carry, negotiated at handshake, is the M**\_\_** (segment size).

**Answer:** MSS — Maximum Segment Size.

**Q88. [Hard]** Path MTU Discovery is used to:

- A) Find DNS servers
- B) Determine the largest packet size without fragmentation along a path
- C) Encrypt data
- D) Resolve ARP

**Answer:** B — It avoids fragmentation by finding the path MTU.

**Q89. [Hard]** Why is UDP preferred for DNS queries (typically)?

- A) Reliability
- B) Low overhead/latency; queries are small and can be retried
- C) Encryption
- D) Ordering

**Answer:** B — UDP's low overhead suits small, fast lookups (TCP for large/zone transfers).

**Q90. [Hard] (True/False)** TCP's sliding window enables multiple in-flight segments before requiring an ACK.

**Answer:** True — The window allows pipelining for throughput.

**Q91. [Hard]** A routing loop is prevented in distance-vector protocols by:

- A) TTL only
- B) Split horizon, poison reverse, and hold-down timers
- C) ARP
- D) NAT

**Answer:** B — These techniques prevent count-to-infinity loops.

**Q92. [Hard]** In TLS 1.3, the handshake is faster because it:

- A) Uses more round trips
- B) Reduces to 1-RTT (or 0-RTT resumption) and removes legacy features
- C) Drops encryption
- D) Uses UDP only

**Answer:** B — TLS 1.3 streamlines the handshake to 1-RTT.

**Q93. [Hard] (One word)** The technique of hiding many internal hosts behind one public IP using ports is called **\_\_** (a.k.a. PAT/overload NAT).

**Answer:** PAT — Port Address Translation (NAT overload).

**Q94. [Hard]** Which best explains why fragmentation is discouraged?

- A) It's illegal
- B) It increases overhead and a single lost fragment forces retransmission of the whole datagram
- C) It speeds up delivery
- D) It encrypts data

**Answer:** B — Lost fragments waste the entire datagram.

**Q95. [Hard]** The difference between a stateful and stateless firewall is:

- A) Stateful tracks connection state; stateless filters per-packet by rules
- B) They are identical
- C) Stateless tracks state
- D) Stateful ignores connections

**Answer:** A — Stateful inspects connection context; stateless matches rules only.

**Q96. [Hard] (True/False)** BGP makes routing decisions primarily based on policy and path attributes, not just shortest hop count.

**Answer:** True — BGP uses AS-path and policies, not pure metrics.

**Q97. [Hard]** In CSMA/CD (classic Ethernet), CD stands for:

- A) Collision Detection
- B) Carrier Data
- C) Collision Domain
- D) Connection Drop

**Answer:** A — Collision Detection handles simultaneous transmissions.

**Q98. [Hard]** Why is CSMA/CD largely obsolete in modern switched Ethernet?

- A) Slower networks
- B) Full-duplex switched links eliminate collisions
- C) UDP replaced it
- D) IPv6

**Answer:** B — Dedicated full-duplex links remove collisions.

**Q99. [Hard] (One word)** The DNS security extension that adds cryptographic signatures to prevent spoofing is DNS**\_\_**.

**Answer:** SEC — DNSSEC signs records to ensure authenticity.

**Q100. [Hard]** The "count-to-infinity" problem is associated with:

- A) Link-state protocols
- B) Distance-vector protocols
- C) TCP
- D) DNS

**Answer:** B — Distance-vector protocols can slowly converge, counting to infinity.
