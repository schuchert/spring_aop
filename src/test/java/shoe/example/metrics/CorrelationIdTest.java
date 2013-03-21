package shoe.example.metrics;

import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class CorrelationIdTest {
  int id(String correlationId) {
    return Integer.parseInt(correlationId.split("[.]")[0]);
  }

  int level(String correlationId) {
    return Integer.parseInt(correlationId.split("[.]")[1]);
  }

  @Test
  public void initialLevelIs1() {
    CorrelationId.enter();

    try {
      assertThat(level(CorrelationId.get()), is(1));
    } finally {
      CorrelationId.exit();
    }
  }

  @Test
  public void enterIncrements() {
    CorrelationId.enter();
    CorrelationId.enter();

    try {
      assertThat(level(CorrelationId.get()), is(2));
    } finally {
      CorrelationId.exit();
      CorrelationId.exit();
    }

  }

  @Test(expected = RuntimeException.class)
  public void cannotGetAfterLastExit() {
    CorrelationId.enter();
    CorrelationId.exit();
    CorrelationId.get();
  }

  @Test(expected = RuntimeException.class)
  public void exitBeforeEnterFails() {
    CorrelationId.exit();
  }

  @Test
  public void enterInAnotherThreadDoesNotChangeThisThread() throws Exception {
    CorrelationId.enter();
    String original = CorrelationId.get();

    Thread thread = new Thread(new Runnable() {
      public void run() {
        CorrelationId.enter();
        CorrelationId.exit();
      }
    });
    thread.start();
    thread.join();

    try {
      assertThat(CorrelationId.get(), is(original));
    } finally {
      CorrelationId.exit();
    }
  }

  @Test
  public void idsInDifferentThreadsAreNotEqual() throws Exception {
    CorrelationId.enter();
    final int firstThreadId = id(CorrelationId.get());

    final boolean[] sameId = {true};

    Thread thread = new Thread(new Runnable() {
      public void run() {
        CorrelationId.enter();
        sameId[0] = firstThreadId == id(CorrelationId.get());
        CorrelationId.exit();
      }
    });

    thread.start();
    thread.join();

    try {
      assertFalse(sameId[0]);
    } finally {
      CorrelationId.exit();
    }
  }
}
