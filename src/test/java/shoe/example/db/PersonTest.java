package shoe.example.db;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import shoe.example.log.SystemLoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.logging.Level;

public class PersonTest {
  private EntityManagerFactory emf;
  private EntityManager em;

  static {
    SystemLoggerFactory.setLevel("org", Level.SEVERE);
  }

  @Before
  public void initEmfAndEm() {
    emf = Persistence.createEntityManagerFactory("examplePersistenceUnit");
    em = emf.createEntityManager();
  }

  @After
  public void cleanup() {
    em.close();
  }

  @Test
  public void emptyTest() {
  }
}
