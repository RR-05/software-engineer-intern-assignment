Durable Execution Engine: Assignment 1


📌 Project Concept
In a distributed system, transient failures are inevitable. This project provides a Native Durable Execution Engine designed for Resiliency. The engine ensures workflows survive process crashes and resume execution without repeating expensive operations.

-------------------------------------------------------------------------------------------------------------------------------
🏗️ Architectural Overview
->State Management & Persistence: Uses SQLite for atomic commits. I utilized Jackson to serialize Java return types into JSON strings, ensuring the engine is data-agnostic.

->The Step Primitive (Memoization): Implements a pattern where the engine "interrogates" the DB before execution. If a step_key exists, it skips execution; otherwise, it triggers the functional interface and persists the result.

->Logical Clock: A Sequence ID counter acts as a logical clock to ensure determinism, even in loops (e.g., process-payment-0, process-payment-1).

->Concurrency: Uses CompletableFuture to handle parallel branches (laptop and access provisioning) with independent durability.
-------------------------------------------------------------------------------------------------------------------------------------

💻 Installation & How to Run
1. Prerequisites

->Java 17 or higher

->Maven 3.6+
------------------------------------------------------------------------------------------------------------
2. Setup

```bash
git clone https://github.com/RR-05/software-engineer-intern-assignment.git
cd assignment1-durable-engine
```
------------------------------------------------------------------------------------------------------------
3. Execution

```bash
mvn clean compile exec:java -Dexec.mainClass="com.assignment.engine.App"

```
-----------------------------------------------------------------------------------------------------------
🧪 Verification & Crash Recovery
The engine's durability is proven through a simulated failure:

Initial Run: Executes create-record and provision-laptop.

Simulated Crash: System.exit(0) is triggered.

Recovery: Upon restart, logs confirm the engine skips completed steps:

```bash
>>> \[RESUMING] Skipping: create-record

>>> \[RESUMING] Skipping: provision-laptop
```
------------------------------------------------------------------------------------------------------------------------------------
🛠️ Technology Stack
Language: Java 17

Build Tool: Maven

Database: SQLite (JDBC)

JSON Library: Jackson Databind





