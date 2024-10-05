# credit-card-request-service

### Overview
This project implements a credit card application processing system that evaluates applicants based on various verification checks, risk assessments, and behavioral analyses. The system supports sequential step execution and provides outcomes based on predefined scoring logic.

### Project Structure Overview
1. Credit Card Request - This package is responsible for handling the initial customer request for a credit card. It interacts with the user interface (UI) to collect customer details and save them for further processing.
2. Credit Card Request Processor - This package handles the core processing logic for credit card applications. Based on the customer details saved earlier, it executes a series of steps to determine the outcome of the application, such as whether the card is approved or rejected.
3. Score Verification - This package contains all the external checks and verification logic. It's further divided into various sub-packages, each implementing a specific type of check.

### Scoring Logic and Outcomes:
1. Mandatory Check (Identity Verification):
    - If Identity Verification is Successful, the application proceeds to further scoring.
    - If Identity Verification is Rejected, the application is immediately rejected, regardless of other scores.
2. Scoring Adjustments:
    - If Mandatory Check is Successful, the system continues evaluating the other categories (Compliance Check, Employment Verification, Risk Evaluation, Behavioral Analysis) to calculate the total score
3. Outcome Thresholds:
    - Above 90% Total Score (with Identity Verification "Yes"): STP (Straight-Through Processing) — The card is issued automatically.
    - 75% - 90% Total Score (with Identity Verification "Yes"): Near-STP — The card is issued automatically, but the credit limit is set manually after a review.
    - 50% - 75% Total Score (with Identity Verification "Yes"): Manual Review — The application goes for further assessment by an underwriter.
    - Below 50% Total Score or Identity Verification "No": Rejected — The application is automatically rejected.