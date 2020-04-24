# Database

`This scheme is not impelemented yet!`

![LearnBrailleDatabase](https://user-images.githubusercontent.com/25281147/80180180-35014e00-8613-11ea-9c3d-dbe75a99e816.png)


```
Table users as U {
  id long [pk, increment]
  name varchar [not null]
  login varchar [not null]
}

Table known_cards as KS {
  user_id long [pk, ref: > U.id]
  card_id long [pk, ref: > C.id]
}


Table courses {
  id long [pk]
  name varchar [not null]
  description varchar [not null, default: '']
}

Table lessons as L {
  id long [pk]
  name varchar [not null]
  description varchar [not null, default: '']
  course_id long [not null, ref: > courses.id]
}

Table steps as S {
  id long [pk, increment]
  lesson_id long [not null, ref: > L.id]
  card_id long [not null, ref: > C.id]
}


Table decks as D {
  id long [pk]
  name varchar [not null]
  description varchar [not null, default: '']
}

Table cards as C {
  id long [pk, increment]
  data varchar [not null, note: 'Serialized jvm class']
  deck_id long [ref: > D.id]
}


Table annotations as A {
  id long [pk, increment]
  name varchar [not null, unique]
}

Table card_annotations as CA {
  card_id long [pk, ref: > C.id]
  annotation_id long [pk, ref: > A.id]
}
```
