package ru.javaops.masterjava;

import com.google.common.io.Resources;
import ru.javaops.masterjava.xml.schema.GroupType;
import ru.javaops.masterjava.xml.schema.ObjectFactory;
import ru.javaops.masterjava.xml.schema.Payload;
import ru.javaops.masterjava.xml.schema.ProjectType;
import ru.javaops.masterjava.xml.schema.User;
import ru.javaops.masterjava.xml.util.JaxbParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;

public class MainXML {
    public static void main(String[] args) throws IOException, JAXBException {
        if (args.length < 1) {
            System.out.println("It is necessary to pass the parameter \"projectName\"");
            return;
        }

        String projectName = args[0];

        JaxbParser parser = new JaxbParser(ObjectFactory.class);
        Payload payload = parser.unmarshal(Resources.getResource("payload.xml").openStream());
        List<ProjectType> projects = payload.getProjects().getProject();
        Optional<ProjectType> optionalProject = projects.stream()
                .filter(p -> projectName.equals(p.getName()))
                .findAny();
        if (!optionalProject.isPresent()) {
            System.out.println("Project with name " + projectName + " not found");
            return;
        }

        ProjectType project = optionalProject.get();
        List<GroupType> projectGroups = project.getGroup();

        List<User> users = payload.getUsers().getUser();

        TreeSet<String> fullNames = new TreeSet<>();

        for (User user : users) {
            for (Object obj : user.getGroupTypeRefs()) {
                GroupType group = (GroupType) obj;
                if (projectGroups.contains(group)) {
                    fullNames.add(user.getFullName());
                    break;
                }
            }
        }

        fullNames.forEach(System.out::println);
    }
}
