package shoe.example.context;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SystemApplicationContext implements ApplicationContextAware, BeanFactoryAware {
  private static ApplicationContext applicationContext;
  private static BeanFactory beanFactory;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    SystemApplicationContext.applicationContext = applicationContext;
  }

  @Override
  public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
    SystemApplicationContext.beanFactory = beanFactory;
  }

  public static ApplicationContext getApplicationContext() {
    verifyImInitialized();
    return applicationContext;
  }

  public static BeanFactory getBeanFactory() {
    verifyImInitialized();
    return beanFactory;
  }

  public static boolean isInitialized() {
    return beanFactory != null && applicationContext != null;
  }

  private static void verifyImInitialized() {
    if(!isInitialized()) {
      throw new RuntimeException("Application Context Not Initialized: Was I constructed via spring?");
    }
  }

}
