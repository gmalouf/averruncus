Averruncus
==========

Averruncus is a library for validating that a password meets a high standard of security.  It is implemented in Scala, though the approach should be transferrable to many other languages.

[![Circle CI](https://circleci.com/gh/gmalouf/averruncus.svg?style=svg)](https://circleci.com/gh/gmalouf/averruncus)

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

- Accept username/email address optionally and check that variations of it are not contained in password
- Regexes preventing the most commons topologies of passwords i.e. 'Broncos1!' style (see video/linked articles)
- Min topology change between old/new passwords
- Allowing loading of lists of most widely used passwords on internet to be explicitly blacklisted

Relevant Posts/Articles
---------------------

- [Unmasked: What 10 million passwords reveal about the people who chose them](http://wpengine.com/unmasked/)
- [Your Clever Password Tricks Aren't Protecting You from Today's Hackers](http://lifehacker.com/5937303/your-clever-password-tricks-arent-protecting-you-from-todays-hackers)
- [OWASP: Implement Proper Password Strength Controls](https://www.owasp.org/index.php/Authentication_Cheat_Sheet#Implement_Proper_Password_Strength_Controls)