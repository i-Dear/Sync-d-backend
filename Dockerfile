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

ENV GOOGLE_CLIENT_ID="70988875044-9nmbvd2suleub4ja095mrh83qbi7140j.apps.googleusercontent.com"
ENV GOOGLE_CLIENT_SECRET="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"

# Make port 8080 available to the world outside this container
EXPOSE 8080

# Run the jar file
CMD ["java", "-jar", "syncd-0.0.1-SNAPSHOT.jar"]
