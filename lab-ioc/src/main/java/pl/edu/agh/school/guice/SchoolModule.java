package pl.edu.agh.school.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import pl.edu.agh.logger.ConsoleMessageSerializer;
import pl.edu.agh.logger.FileMessageSerializer;
import pl.edu.agh.logger.Logger;
import pl.edu.agh.school.School;
import pl.edu.agh.school.SchoolDAO;
import pl.edu.agh.school.persistence.PersistenceManager;
import pl.edu.agh.school.persistence.SerializablePersistenceManager;

public class SchoolModule extends AbstractModule {
//    private final String classFile;
//    private final String teacherFile;
//
//    public SchoolModule(String classFile, String teacherFile) {
//        this.classFile = classFile;
//        this.teacherFile = teacherFile;
//    }

    @Override
    protected void configure() {
        bind(String.class)
                .annotatedWith(Names.named("classFile"))
                .toInstance("classes456.dat");
        bind(String.class).annotatedWith(Names.named("teacherFile"))
                .toInstance("teacher456.dat");
        //        bind(String.class)
//                .annotatedWith(Names.named("classFile"))
//                .to(classFile);
//        bind(School.class).annotatedWith(Names.named("teacherFile"))
//                .to(teacherFile);
    }
    @Provides
    PersistenceManager providePersistenceManager(SerializablePersistenceManager persistenceManager) {
        return persistenceManager;
    }
    @Provides
    @Singleton
    Logger provideFileLogger() {
        Logger logger = new Logger();

        logger.registerSerializer(new ConsoleMessageSerializer());
        logger.registerSerializer(new FileMessageSerializer("persistence.log"));
        return logger;
    }


}
