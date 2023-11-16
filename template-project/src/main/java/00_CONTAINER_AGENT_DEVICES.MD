# Classes
## 1. Connexion/Container.java
Initialization of AGENTS
Is inside the `Connexion` folder

## 2. Connexion/Agent.java
Agents makes use of Devices  
Is inside the `Connexion` folder  
Can be moved to your own `Agents` folder
### 2.1 Behaviours
1. CyclicBehaviour
2. OneShotBehaviour
3. SequentialBehaviour

### 2.2 BEHAVIOUR.addSubBehavior

## 3. Device.java
1. Initialize all agents in the Container
2. Each agents can make use of all the devices in (here Layer1_Dev folder)

# RUNNING
1. Go to `Connection` folder and run `Container1`, this is initializes the interface
2. Go to `main/resources/META-INF/MANIFEST.MF` and change `Main-Class: ` to your own `Container` (e.g. Container2)

# Useful functions
```java
    ACLMessage aclMessage = receive();
    if (aclMessage != null) {
        int number = Integer.parseInt(aclMessage.getContent());
    }
```
