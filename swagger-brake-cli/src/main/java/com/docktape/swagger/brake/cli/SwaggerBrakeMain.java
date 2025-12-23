package com.docktape.swagger.brake.cli;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.SimpleCommandLinePropertySource;

@SuppressFBWarnings("DM_EXIT")
@Slf4j
public class SwaggerBrakeMain {
    /**
     * The main entrypoint for the CLI interface.
     * @param args the arguments
     */
    public static void main(String[] args) {
        Cli cli = createCliInterface(args);
        int exitCode = cli.start();
        if (exitCode > 0) {
            log.error("Failed with Exiting with error code {}", exitCode);
            System.exit(exitCode);
        }
        log.info("Successful Exiting with code {}", exitCode);
    }

    /**
     * Constructs the CLI interface.
     * @param args the arguments
     * @return the CLI interface object
     */
    public static Cli createCliInterface(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(CliConfiguration.class);
        ConfigurableEnvironment environment = context.getEnvironment();
        environment.getPropertySources().addFirst(new SimpleCommandLinePropertySource(args));
        return context.getBean(Cli.class);
    }
}
