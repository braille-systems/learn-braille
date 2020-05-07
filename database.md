# Database

![LearnBrailleDatabase](https://user-images.githubusercontent.com/25281147/81062750-982e9280-8ee7-11ea-849a-cca071f36a09.png)


By `dbdiagram.io`:

```
// All fields [not null] by default


Table users as U {
  id long [pk, increment]
  name varchar
  login varchar
}

Table known_materials as KM {
  user_id long [pk, ref: > U.id]
  material_id long [pk, ref: > M.id]
}

Table materials as M {
  id long [pk]
  material_data varchar [note: 'serialized MaterialData class']
}

Table current_step {
  user_id long [pk, ref: > U.id]
  course_id long [pk, ref: > courses.id]
  step_id long [ref: > S.id]
}

Table last_course_step {
  user_id long [pk, ref: > U.id]
  course_id long [pk, ref: > courses.id]
  step_id long [ref: > S.id]
}

Table last_lesson_step {
  user_id long [pk, ref: > U.id]
  course_id long [pk, ref: > courses.id]
  lesson_d long [pk, ref: > lessons.id]
  step_id long [ref: > S.id]
}




Table courses {
  id long [pk]
  name varchar
  description varchar
}

Table lessons as L {
  id long [pk]
  name varchar
  description varchar [default: '']
  course_id long [ref: > courses.id]
}

// StepData also contains material.id sometimes
// to be able to mark material as known
Table steps as S {
  id long [pk]
  step_data varchar [note: 'serialized StepData']
  lesson_id long [ref: > L.id]
}

Table step_annotations {
  step_id long [pk, ref: > S.id]
  annotation_id long [pk, ref: > A.id]
}

Table annotations as A {
  id long [pk, increment]
  name varchar
}



Table decks as D {
  id long [pk]
  name varchar [not null]
  description varchar [not null, default: '']
}

Table cards as C {
  deck_id long [pk, ref: > D.id]
  material_id long [pk, ref: > M.id]
}
```
