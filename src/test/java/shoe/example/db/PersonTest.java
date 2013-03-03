package shoe.example.db;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class PersonTest {
  private EntityManagerFactory emf;
  private EntityManager em;

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
