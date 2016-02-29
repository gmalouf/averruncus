Averruncus
==========

Averruncus is a library for validating that a password meets a high standard of security.  It is implemented in Scala, though the approach should be transferrable to many other languages.

Inspiration
-----------

Rick Redman's talk ["Your Password Complexity Requirements are Worthless"](https://www.youtube.com/watch?v=zUM7i8fsf0g) at AppSecUSA 2014 was the catalyst for building this project. 

Getting Averruncus
------------------
**TODO**

Usage
-----

**TODO**


TODO List
-------

- Abstract single function tests out to be re-used w/ validatePassword
- Check/Failure for whitespace character in passwords (or start/end of passwords)
- Regexes enforcing Owasp standards (enforcing character class uses)
- Regexes preventing the most commons patterns of passwords (see video/linked articles)
- Allowing loading of lists of most widely used passwords on internet