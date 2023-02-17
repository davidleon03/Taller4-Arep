# REPO BASE JAVA Y MVN
System.out.println(Arrays.toString(methods.stream()
                .filter(method -> method.isAnnotationPresent(RequestMapping.class)).toArray()));