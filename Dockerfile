# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-oracle

# Set the working directory to /app
WORKDIR /app

# Copy the jar file into the image
COPY ./build/libs/syncd-0.0.1-SNAPSHOT.jar /app/syncd-0.0.1-SNAPSHOT.jar
# Environment variables for MongoDB configuration
ENV SPRING_DATA_MONGODB_URI="mongodb+srv://dongjae:qwe1356@syncd.m98ytka.mongodb.net/?retryWrites=true&w=majority"
ENV SPRING_DATA_MONGODB_DATABASE="syncd"

# Environment variables for Spring security
ENV SPRING_SECURITY_AUTH_LIVEBLOCKSECRETKEY="sk_dev_q7DugH6NAP_T02pSlEBP79sOujz_tqvM9Tf5eA8doLL1nrjyuASP0KPRvp0hGpYr"

# Make port 8080 available to the world outside this container
EXPOSE 8080

# Run the jar file
CMD ["java", "-jar", "syncd-0.0.1-SNAPSHOT.jar"]
