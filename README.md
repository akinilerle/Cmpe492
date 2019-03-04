# Cmpe492
A tool for checking the quality of user stories in Turkish.

### Quality Metrics
A user story should be:
* Minimal
There should not be additional notes about the story. Violating Example: "Bir sistem yöneticisi olarak, şifreleri sıfırlamak istiyorum. Not: yeni şifre rastgele oluşturulmalı ve mail olarak yollanmalı."

* Unambiguous
The user story should be clear. Violating Example: "Bir kullanıcı olarak, bir profil eklediğim içeriği değiştirebilmek istiyorum."

* A Full Sentence
The user story should be a full sentence without syntactic errors. It should have a subject and a verb. Violating Example: "Şifrelerin hashlenmesi"

* Atomic
The story should have one verb and it should not contain any conjunction words.  Violating Example: "Kullanıcı olarak, ödememi kredi kartı ile, EFT ile veya kapıda ödeyerek yapmak istiyorum."

* Unique
A requirement should have only one user story.

* Uniform
The story should follow the given user story template: "Bir \<ROL\> olarak, \<ISTEK\> istiyorum. [Böylece, <SEBEP>]". Violating Example: "Bir kullanıcı olarak, yeni mail geldiğinde bildirim almalıyım."

* Independent
Another user story should not be referenced.

### Mock-up

![Mock-up](https://raw.githubusercontent.com/akinilerle/Cmpe492/master/media/mock-up.png)

### References

* Lucassen, Garm, Fabiano Dalpiaz, Jan Martijn E. M. Van Der Werf, and Sjaak Brinkkemper. "Improving Agile Requirements: The Quality User Story Framework and Tool." Requirements Engineering 21, no. 3 (2016): 383-403. doi:10.1007/s00766-016-0250-x.
