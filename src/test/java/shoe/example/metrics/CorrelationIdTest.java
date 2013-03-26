package shoe.example.metrics;

import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class CorrelationIdTest {
  CorrelationId id = new CorrelationId();

  int id(String correlationId) {
    return Integer.parseInt(correlationId.split("[.]")[0]);
  }

  int level(String correlationId) {
    return Integer.parseInt(correlationId.split("[.]")[1]);
  }

  @Test
  public void initialLevelIs1() {
    id.enter();

    try {
      assertThat(level(id.get()), is(1));
    } finally {
      id.exit();
    }
  }

  @Test
  public void enterIncrements() {
    id.enter();
    id.enter();

    try {
      assertThat(level(id.get()), is(2));
    } finally {
      id.exit();
      id.exit();
    }

  }

  @Test(expected = RuntimeException.class)
  public void cannotGetAfterLastExit() {
    id.enter();
    id.exit();
    id.get();
  }

  @Test(expected = RuntimeException.class)
  public void exitBeforeEnterFails() {
    id.exit();
  }

  @Test
  public void enterInAnotherThreadDoesNotChangeThisThread() throws Exception {
    id.enter();
    String original = id.get();

    Thread thread = new Thread(new Runnable() {
      public void run() {
        id.enter();
        id.exit();
      }
    });
    thread.start();
    thread.join();

    try {
      assertThat(id.get(), is(original));
    } finally {
      id.exit();
    }
  }

  @Test
  public void idsInDifferentThreadsAreNotEqual() throws Exception {
    id.enter();
    final int firstThreadId = id(id.get());

    final boolean[] sameId = {true};

    Thread thread = new Thread(new Runnable() {
      public void run() {
        id.enter();
        sameId[0] = firstThreadId == id(id.get());
        id.exit();
      }
    });

    thread.start();
    thread.join();

    try {
      assertFalse(sameId[0]);
    } finally {
      id.exit();
    }
  }
}
