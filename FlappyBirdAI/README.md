## FlappyBird - Artificial Intelligence

Game Auto-learning using Neural Networks

[![FlappyBirdAI](https://github.com/GustavoRolimSantos/Java/blob/master/FlappyBirdAI/data/FlappyBird-demo.gif)](https://www.youtube.com/watch?v=eHvFdgZAawI)

### About the project
The idea is to use Neural Networks to find the best Bird by using Natural Selection (Random Mutations).<br>
The game was recreated from scratch (without engines) using Java.

The Neural Network was created with 3 layers and Sigmoid as the activation function:
- Input Layer with 4 sensors
- Hidden layer with 4 neurons + 1 bias
- Output layer with 1 neuron (Jump)

The population size used to train was between 1000-2000 birds.

The learning time was between 5 and 10 minutes.

**For more information watch the [demonstration video](https://www.youtube.com/watch?v=eHvFdgZAawI).** 

### Code
Inside the "data" folder there is another folder called "synapses", the file inside this folder is the training.
Delete this file if you want to train the Neural Network again.

> COPYRIGHT BY GUSTAVO ROLIM DOS SANTOS 2020.