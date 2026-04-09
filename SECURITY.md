# Security Policy & Responsible Disclosure

Thank you for taking the time to help improve the security of this project. This document explains what we consider a vulnerability, how to report it securely, and how we handle disclosures.

## What is a vulnerability?

A vulnerability is any behavior, bug, or configuration that may compromise:

- the confidentiality, integrity, or availability of data;
- authentication or authorization (e.g., bypass, privilege escalation);
- remote code execution (RCE), injection flaws (SQLi, XSS), or disclosure of sensitive information;
- any issue that allows unauthorized access, modification, or deletion of data.

## How to report a vulnerability (preferred and secure option)

1. Preferred: use GitHub Security Advisories (recommended):

   - [https://github.com/alan-altruy/calculator-2026-group1/security/advisories/new]

2. Alternatively, send an encrypted email using OpenPGP to: mailto:alan.evy@gmail.com
   - Encrypt the report with OpenPGP before sending. Example:

   ```bash
   # encrypt report.txt to the maintainer's public key
   gpg --encrypt --recipient "alan.evy@gmail.com" --armor -o report.txt.asc report.txt
   ```

   - Attach the encrypted file `report.txt.asc` to the email. Do NOT send exploits or PoCs in plaintext.

3. Public PGP key: we publish our OpenPGP public key in the repository so you can encrypt reports:
   - [https://github.com/alan-altruy/calculator-2026-group1/blob/main/SECURITY-KEY.asc]
   - Use that key to encrypt your report before sending.

4. If you need to share a larger proof-of-concept or artifacts, state that in the report and we will arrange a secure channel.

## What to include in your report

To help us reproduce and fix the issue quickly, please include:

- a clear description of the issue and its impact;
- affected versions (commit / tag / release);
- step-by-step reproduction steps (commands, inputs, environment);
- a minimal proof-of-concept (PoC) if possible;
- relevant logs, screenshots, or dumps;
- a secure contact method (encrypted email address) so we can follow up.

## Our commitment

- Acknowledgement within 72 business hours.
- Initial assessment and proposed plan within 7 business days when feasible.
- We aim to provide a fix or mitigation within a reasonable timeframe (typically 90 days) and will coordinate the public disclosure date with the reporter.

## Confidentiality and coordinated disclosure

Please do not publicly disclose the vulnerability without our consent. We prefer coordinated disclosure and will work with you to publish a joint advisory when a fix is available.

## Exclusions

Non-security bug reports, feature requests, or general questions should be filed through the public issue tracker.

## Useful resources

- Coordinated disclosure guide: [https://www.first.org/resources/guides](https://www.first.org/resources/guides)
- GitHub Security Advisories: [https://docs.github.com/en/code-security/security-advisories](https://docs.github.com/en/code-security/security-advisories)

Thank you for helping to keep this project secure.
