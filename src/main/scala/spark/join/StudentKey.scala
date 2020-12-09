package spark.join

/**
 * @Author liu.teng
 * @Date 2020/12/9 10:20
 * @Version 1.0
 */

//创建key类，key组合键为grade，score
case class StudentKey(grade: String, score: Int)

object StudentKey {
  implicit def orderingByGradeStudentScore[A <: StudentKey]: Ordering[A] = {
    Ordering.by(fk => (fk.grade, fk.score * -1))
  }

}


