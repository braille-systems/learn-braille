# Database

By `dbdiagram.io`:

![LBDB](https://user-images.githubusercontent.com/25281147/81498004-5a5cb000-92d3-11ea-9c53-7246ef9d0176.png)

```
// All fields [not null] by default


Table users {
  id long [pk, increment]
  name varchar
  login varchar
}

Table known_materials {
  user_id long [pk, ref: > users.id]
  material_id long [pk, ref: > materials.id]
}

Table materials {
  id long [pk]
  material_data varchar [note: 'serialized MaterialData class']
}

Table current_step {
  user_id long [pk, ref: > users.id]
  course_id long [pk, ref: > steps.course_id]
  lesson_id long [ref: > steps.lesson_id]
  step_id long [ref: > steps.id]
}

Table last_course_step {
  user_id long [pk, ref: > users.id]
  course_id long [pk, ref: > steps.course_id]
  lesson_id long [ref: > steps.lesson_id]
  step_id long [ref: > steps.id]
}

Table last_lesson_step {
  user_id long [pk, ref: > users.id]
  course_id long [pk, ref: > steps.course_id]
  lesson_id long [pk, ref: > steps.lesson_id]
  step_id long [ref: > steps.id]
}


Table courses {
  id long [pk]
  name varchar
  description varchar
}

Table lessons {
  id long [pk]
  course_id long [pk, ref: > courses.id]
  name varchar
  description varchar [default: '']
}

// StepData also contains material.id sometimes
// to be able to mark material as known
Table steps {
  id long [pk]
  course_id long [pk, ref: > courses.id]
  lesson_id long [pk, ref: > lessons.id]
  step_data varchar [note: 'serialized StepData']
}

Table step_has_annotations {
  course_id long [pk, ref: > steps.course_id]
  lesson_id long [pk, ref: > steps.lesson_id]
  step_id long [pk, ref: > steps.id]
  annotation_id long [pk, ref: > step_annotations.id]
}

Table step_annotations {
  id long [pk, increment]
  name varchar
}


Table decks {
  id long [pk]
  name varchar [not null]
}

Table cards {
  deck_id long [pk, ref: > decks.id]
  material_id long [pk, ref: > materials.id]
}
```

Update: `decks` does not have description.
