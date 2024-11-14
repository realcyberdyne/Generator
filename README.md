# How use Generator

### 1-Install JDK 17
```
sudo apt install openjdk-17-jdk
```

### 2-Install Maven On System
```
sudo apt install maven
```

### 3-Clone Project to system
```
git clone https://github.com/realcyberdyne/Generator.git
```

### 4-Run Project
```
mvn exec:java
```
![image](https://github.com/user-attachments/assets/f2897d20-761d-4ab3-b3d7-dda37f770f7a)





#Linux Service
```
[Unit]
Description=Generator service
After=network.target

[Service]
ExecStart=java -jar /home/rezafta/Documents/Generator/Cyberdyne-1.0-SNAPSHOT.jar
Restart=always
Type=simple

[Install]
WantedBy=multi-user.target
```






# Use in nginx
```
server {
    server_name localhost;

    location / {
        proxy_pass http://127.0.0.1:8085;  # Assuming your Spring Boot app runs>
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    }
}

```
