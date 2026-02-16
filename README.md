Durable Execution Engine: Implementation of Assignment 1



📌 Project Concept

In a distributed system, transient failures are inevitable. This project addresses that by providing a Native Durable Execution Engine. The core value proposition is Resiliency: the ability for a workflow to survive process crashes and resume execution without repeating expensive or side-effect-heavy operations.



🏗️ Architectural Overview

1\. State Management \& Persistence (RDBMS)

I chose SQLite for the persistence layer to meet the requirement of using a relational database.



Atomic Commit: Every successful step execution is committed to the workflow\_steps table.



Serialization: I utilized Jackson to serialize Java return types into JSON strings. This ensures that the engine is data-agnostic and can store complex objects as simple text.



2\. The Step Primitive (Memoization Logic)

The step method is the primary building block. I implemented a Memoization pattern where the engine first "interrogates" the database.



Lookup: If a step\_key exists for the current workflow\_id, the engine avoids re-execution.



Execution: If no record is found, the functional interface is triggered, and the result is persisted before moving forward.



3\. Logical Clock for Determinism

To handle workflows that might involve loops or repeated logic, I implemented a Sequence ID counter.



This acts as a logical clock, ensuring that even if a step has the same name (e.g., "process-payment"), its specific occurrence is uniquely identified (e.g., process-payment-0, process-payment-1).



4\. Concurrency \& Parallelism

The Onboarding workflow requires parallel execution for laptop and access provisioning.



I utilized CompletableFuture to handle these concurrent branches.



The engine is designed to handle these simultaneous database writes, ensuring each parallel branch is independently durable.


---

## 💻 Installation & How to Run

Follow these steps to build and execute the Durable Engine on your local machine:

### 1. Prerequisites
* **Java 17** or higher
* **Maven 3.6+**

### 2. Setup
Clone the repository and navigate to the project directory:
```bash
git clone https://github.com/RR-05/software-engineer-intern-assignment.git
cd assignment1-durable-engine



🧪 Verification \& Crash Recovery

The effectiveness of this engine is best demonstrated through a failure scenario.



Initial Run: The workflow executes create-record and provision-laptop.



Simulated Crash: A System.exit(0) is triggered.



Recovery Run: Upon restart, the logs confirm that the engine recognizes the persisted state:



>>> \[RESUMING] Skipping: create-record



>>> \[RESUMING] Skipping: provision-laptop



Resumption: The engine picks up exactly at provision-access, fulfilling the "Durable" requirement.



🛠️ Technology Stack

Language: Java 17



Build Tool: Maven



Database: SQLite (JDBC)



JSON Library: Jackson Databind



