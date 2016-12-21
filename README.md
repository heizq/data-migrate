# data-migrate
### data migrate ,move data to another place;
technique tools：
- mongodb
- redis
- spring-jdbc
- logback
- protostuff
- apache.commons
- HikariCP
- guava

  数据迁移，工具使用说明:

  零、
  数据准备：如果图片没有上传到mongodb，首先要执行ImageApp,
  在ImageApp class 中，如果数据库中没有创建存放图片mongodb id 的临时表，
  需要先调用createTempTable() 方法，创建临时表。

  一、
  使用DataMigrate 之前，需要首先执行 DataPlay 让数据上场。

  二、
  执行完DataMigrate之后，需要人工将以下表的数据转移到新库中去；
  ks_chapter_section
  ks_chapter_section_knowledge
  ks_knowledge_point
  ks_knowledge_point_relation
  ks_paper
  ks_paper_big_item
  ks_paper_small_item
  ks_question_chapter_section
  ks_textbook

