package me.pepyakin.model

import net.liftweb._
import http.{Req, LiftRules}
import org.squeryl.{SessionFactory, Schema}

import squerylrecord.RecordTypeMode._
import java.sql.DriverManager
import util.Props

/**
 * 
 * @author Sergey
 */
object WestrelSchema extends Schema {

  val users = table[User]

//  on(users)(s =>
//    declare(
//      // Имя пользователя должно быть уникально.
//      s.userName is(unique)
//    )
//  )
}

object SchemaHelper {

  /**
   * Дропнуть и создать базу, при этом выводя будующую схему.
   */
  private def dropAndCreate {
    import WestrelSchema._

    printDdl
    drop
    create
  }

  private def fill {
    DemoData.createDemoData
  }

  def dropCreateAndFill {
    inTransaction {
      dropAndCreate
      fill
    }
  }

  def initH2() {

    Class.forName("org.h2.Driver")

    import org.squeryl.adapters.H2Adapter
    import org.squeryl.Session

    SessionFactory.concreteFactory = Some(() =>
      Session.create(
        DriverManager.getConnection("jdbc:h2:mem:dbname;DB_CLOSE_DELAY=-1", "sa", ""),
        new H2Adapter
      )
    )

    if (Props.devMode) {
      LiftRules.liftRequest.append({
        case Req("console" ::_, _, _) => false
      })
    }

    dropCreateAndFill
  }
}