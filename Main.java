import java.util.*;
import java.io.*;
import java.util.concurrent.ThreadLocalRandom;



public class Main
{
    public static double [][] w1;
    public static double [][] w2;
    public static double [][] w3;
    
    
    public static class Joint {
        int result;
        int [] input = new int[128*120];
        File file;
    
        public Joint (File f, int res){
            file = f;
            result = res;
        }
    }
    
    /// Prints the commandline instructions.
    public static void main(String[] args) throws IOException
    {
        int train = 0;
        int test = 0;
        String dir1 = null;
        String dir2 = null;
        String testDir = null;
        
        for(int i = 0; i < args.length; i++){
            if(args[i].equals("-train")){
                train = 1;
                dir1 = args[i+1];
                dir2 = args[i+2];
            }
            if(args[i].equals("-test")){
                test = 1;
                testDir = args[i+1];
            }
        }
        
        if(train == 1){
            //load Male folder
            final File maleFolder = new File (dir1);
            File [] maleFiles = maleFolder.listFiles();
            //load Female folder
            final File femaleFolder = new File (dir2);
            File [] femaleFiles = femaleFolder.listFiles();
            File [] testFiles = null;
                if(test == 1){
                    //load Test folder
                    final File testFolder = new File (testDir);
                    testFiles = testFolder.listFiles();
                }
            
            
            // get 2 arrays - one M, one F
            

            shuffleArray(maleFiles);
            shuffleArray(femaleFiles);// shuffle
            

            int mcount = 0; // split into 5 groups, each is object Joint
            int fcount = 0;
            int total = maleFiles.length + femaleFiles.length;
            
            List<Joint> g1 = new ArrayList<Joint>();
            List<Joint> g2 = new ArrayList<Joint>();
            List<Joint> g3 = new ArrayList<Joint>();
            List<Joint> g4 = new ArrayList<Joint>();
            List<Joint> g5 = new ArrayList<Joint>();
            List<Joint> trainList = new ArrayList<Joint>();
            int rand;
            
            double [] testCorrect = new double [5];
            double [] trainTestCorrect = new double [5];
            
            for (int i = 0; i < total-5; ++i){
                Random random = ThreadLocalRandom.current();
                rand = random.nextInt(2);
                
                
                if (mcount > maleFiles.length - 2){
                    rand = 1;}
                if (fcount > femaleFiles.length - 2) {
                    rand = 0;}
                
                if (maleFiles[mcount].getName() == ".DS_Store"){
                    //mcount++;
                    continue;
                }
                if (femaleFiles[fcount].getName() == ".DS_Store"){
                    //fcount++;
                    continue;
                }
                
                
                switch (i % 5){
                    case 0:
                        if (rand == 0){
                            g1.add(new Joint(maleFiles[mcount], 0));
                            mcount++;
                        }
                        else {
                            g1.add(new Joint(femaleFiles[fcount], 1));
                            fcount++;
                        }
                        break;
                    case 1:
                        if (rand == 0){
                            g2.add(new Joint(maleFiles[mcount], 0));
                            mcount++;
                        }
                        else {
                            g2.add(new Joint(femaleFiles[fcount], 1));
                            fcount++;
                        }
                        break;
                    case 2:
                        if (rand == 0){
                            g3.add(new Joint(maleFiles[mcount], 0));
                            mcount++;
                        }
                        else {
                            g3.add(new Joint(femaleFiles[fcount], 1));
                            fcount++;
                        }
                        break;
                    case 3:
                        if (rand == 0){
                            g4.add(new Joint(maleFiles[mcount], 0));
                            mcount++;
                        }
                        else {
                            g4.add(new Joint(femaleFiles[fcount], 1));
                            fcount++;
                        }
                        break;
                    case 4:
                        if (rand == 0){
                            g5.add(new Joint(maleFiles[mcount], 0));
                            mcount++;
                        }
                        else {
                            g5.add(new Joint(femaleFiles[fcount], 1));
                            fcount++;
                        }
                        break;
                }
                
            }
            w1 = new double[3][15360]; // initialize weights
            w2 = new double[3][3];
            w3 = new double[2][3];
            
            Random r = new Random();
            
            
            for(int i = 0; i < w1.length; i++){
                for(int j = 0; j < w1[0].length; j++){
                    w1[i][j] = 1;
                }
            }
            for(int i = 0; i < w2.length; i++){
                for(int j = 0; j < w2[0].length; j++){
                    w2[i][j] = 1;
                }
            }
            for(int i = 0; i < w3.length; i++){
                for(int j = 0; j < w3[0].length; j++){
                    w3[i][j] = 1;
                }
            }
        
        
            System.out.println("Performing 5-Fold Cross Validation");
            System.out.println("g1 is test");// g1 is test
            train(g2);
            train(g3);
            train(g4);
            train(g5);
            trainList.addAll(g2);
            trainList.addAll(g3);
            trainList.addAll(g4);
            trainList.addAll(g5);
            trainTestCorrect [0] = test(trainList);
            testCorrect [0] = test(g1);
            
            //w1 = new double[3][15361]; // initialize weights
            //w2 = new double[2][4];
            for(int i = 0; i < w1.length; i++){
                for(int j = 0; j < w1[0].length; j++){
                    w1[i][j] = 1;
                }
            }
            for(int i = 0; i < w2.length; i++){
                for(int j = 0; j < w2[0].length; j++){
                    w2[i][j] = 1;
                }
            }
            for(int i = 0; i < w3.length; i++){
                for(int j = 0; j < w3[0].length; j++){
                    w3[i][j] = 1;
                }
            }
             trainList.clear();
            
            System.out.println("g2 is test");// g2 is test
            train(g1);
            train(g3);
            train(g4);
            train(g5);
            trainList.addAll(g1);
            trainList.addAll(g3);
            trainList.addAll(g4);
            trainList.addAll(g5);
            trainTestCorrect [1] = test(trainList);
            testCorrect [1] = test(g2);
            
            //w1 = new double[3][15361]; // initialize weights
            //w2 = new double[2][4];
            for(int i = 0; i < w1.length; i++){
                for(int j = 0; j < w1[0].length; j++){
                    w1[i][j] = 1;
                }
            }
            for(int i = 0; i < w2.length; i++){
                for(int j = 0; j < w2[0].length; j++){
                    w2[i][j] = 1;
                }
            }
            for(int i = 0; i < w3.length; i++){
                for(int j = 0; j < w3[0].length; j++){
                    w3[i][j] = 1;
                }
            }
            
            trainList.clear();
            
            System.out.println("g3 is test");// g3 is test
            train(g1);
            train(g2);
            train(g4);
            train(g5);
            trainList.addAll(g2);
            trainList.addAll(g1);
            trainList.addAll(g4);
            trainList.addAll(g5);
            trainTestCorrect [2] = test(trainList);
            testCorrect [2] = test(g3);
            
            //w1 = new double[3][15361]; // initialize weights
            //w2 = new double[2][4];
            for(int i = 0; i < w1.length; i++){
                for(int j = 0; j < w1[0].length; j++){
                    w1[i][j] = 1;
                }
            }
            for(int i = 0; i < w2.length; i++){
                for(int j = 0; j < w2[0].length; j++){
                    w2[i][j] = 1;
                }
            }
            for(int i = 0; i < w3.length; i++){
                for(int j = 0; j < w3[0].length; j++){
                    w3[i][j] = 1;
                }
            }
            
            trainList.clear();
            
            System.out.println("g4 is test");// g4 is test
            train(g1);
            train(g2);
            train(g3);
            train(g5);
            trainList.addAll(g2);
            trainList.addAll(g3);
            trainList.addAll(g1);
            trainList.addAll(g5);
            trainTestCorrect [3] = test(trainList);
            testCorrect [3] = test(g4);
            
            //w1 = new double[3][15361]; // initialize weights
            //w2 = new double[2][4];
            for(int i = 0; i < w1.length; i++){
                for(int j = 0; j < w1[0].length; j++){
                    w1[i][j] = 1;
                }
            }
            for(int i = 0; i < w2.length; i++){
                for(int j = 0; j < w2[0].length; j++){
                    w2[i][j] = 1;
                }
            }
            for(int i = 0; i < w3.length; i++){
                for(int j = 0; j < w3[0].length; j++){
                    w3[i][j] = 1;
                }
            }
            
            trainList.clear();
            
            System.out.println("g5 is test");// g5 is test
            train(g1);
            train(g2);
            train(g3);
            train(g4);
            trainList.addAll(g2);
            trainList.addAll(g3);
            trainList.addAll(g4);
            trainList.addAll(g1);
            trainTestCorrect [4] = test(trainList);
            testCorrect [4] = test(g5);
            
            double sum = 0;
            double num = 0;
            
            System.out.println("\ntesting data:");
            for (int c = 0; c < 5; ++c){
                sum += testCorrect[c];
            }
            System.out.println("Average is "+sum/5.0);
            
            
            for (int e = 0; e < 5; ++e){
                num += Math.pow((testCorrect[e] - (sum/5.0)), 2);
            }
            num = Math.pow(num/5.0, .5);
            System.out.println("SD is "+num);
            
            sum = 0;
            System.out.println("\ntraining data:");
            for (int d = 0; d < 5; ++d){
                sum += trainTestCorrect[d];
            }
            
            System.out.println("Average is "+sum/5.0);
            
            for (int f = 0; f < 5; ++f){
                num += Math.pow((testCorrect[f] - (sum/5.0)), 2);
            }
            num = Math.pow(num/5.0, .5);
            System.out.println("SD is "+num+"\n");
            
            if(test == 1){
                test(testDir, testFiles);
            }
        }
        
        
    
    

    }
    
    public static void shuffleArray(File[] array) // shuffles arrays of Files
    {
        
        Random random = ThreadLocalRandom.current();
        for (int i = array.length - 1; i > 0; i--)
        {
            int index = random.nextInt(i + 1);
            
            File a = array[index];
            array[index] = array[i];
            array[i] = a;
        }
    }
    
    public static void train (List<Joint> joint) throws IOException {
    
        double z1[] = new double[3]; // #hidden nodes
        double z2[] = new double[3]; // #hidden nodes
        double z3[] = new double[2]; // #final nodes
        
        double a1[] = new double[3]; // #hidden nodes
        double a2[] = new double[3]; // #hidden nodes
        double a3[] = new double[2]; // #final nodes      a2[0] = 1 if male    a2[1] = 1 if female
        
        double l1[] = new double[3]; // learning error for hidden layer
        double l2[] = new double[3]; // learning error for hidden layer
        double l3[] = new double[2]; // learning error for the final layer
        
        
        double learnRate = 1;
        
        double[] normInput = new double[128*120];
        int max, min;
        
        String dir;
        File [] files = new File [joint.size()];
        
        for (int a = 0; a < joint.size(); ++a){
            files[a] = (joint.get(a)).file;
        }
        
        
        
        
        int[] input = new int[128*120];
        
        for (int i = 0; i < files.length; ++i){ // for each training image
            if ((joint.get(i)).result == 0){
                dir = "Male/";
            }
            else{
                dir = "Female/";
            }
            //System.out.println("new image\n");

            FileReader fr = new FileReader(dir+files[i].getName());

            Scanner s = new Scanner(fr);
            
            if (!files[i].getName().endsWith(".txt")) continue;
            
            
            max = 0;
            min = 1000;
            for (int j = 0; j < input.length; j++){
                input[j] = s.nextInt();
                if(input[j] < min)
                    min = input[j];
                if(input[j] > max)
                    max = input[j];
            }
            
            //normalize data
            for(int j =  0; j < input.length; j++){
                normInput[j] = (input[j] - min)/(max - min);

            }
    
            //compute z1
            //System.out.println("Computing z1");
            for(int j = 0; j < w1.length; j++){
                for(int x = 0; x < w1[0].length - 1; x++){
                    z1[j] += w1[j][x]  * normInput[x];
                }
                //System.out.print(z1[j]+" ");
            }
            
            
            // compute a(l) for all layers
            //System.out.println("\nComputing a1");
            for (int j = 0; j < z1.length; j++){
                a1[j] = sigmoid(z1[j]); // apply sigmoid function to z1
                //System.out.print(a1[j]+" ");
            }
            
            //compute z2
            for(int j = 0; j < w2.length; j++){
                for(int x = 0; x <w2[0].length; x++){
                    z2[j] += w2[j][x] * a1[x];
                }
            }
            
            //compute a2
            for(int j = 0; j < z2.length; j++){
                a2[j] = sigmoid(z2[j]);
            }
            
    
            
            //compute z3
            //System.out.println("\nComputing z3");
            for(int j = 0; j < w3.length; j++){
                for(int x = 0; x < w3[0].length; x++){
                    z3[j] += w3[j][x] * a2[x];  // uses weights in second layer to produce z2
                    //System.out.print(z2[j]+" ");
                }
            }
            
            //System.out.println("\nComputing a3");
            for (int k = 0; k < z3.length; ++k){
                a3[k] = sigmoid(z3[k]);
                //System.out.print(a1[k]+" ");
            }
            
            l3[0] = a3[0] - (joint.get(i)).result;
            l3[1] = a3[1] - (joint.get(i)).result;
            
            
            //FIXXXX
            for(int q = 0; q < l2.length; q++){ //compute error for hidden layer
                l2[q] = (w3[0][q]*l3[0] + w3[1][q]*l3[1]) * a2[q] * (1-a2[q]);
            }
            
            for(int q = 0; q < l1.length; q++){ //compute error for hidden layer
                l1[q] = (w2[0][q]*l2[0] + w2[1][q]*l2[1] + w2[2][q]*l2[2]) * a1[q] * (1-a1[q]);
            }
            
            
            
            for(int j = 0; j < w1.length; j++){ // update weights for w1
                for(int x = 0; x < w1[0].length; x++){
                    w1[j][x] -= learnRate * (a1[j] * l1[j]);
                }
            }
            for(int j = 0; j < w2.length; j++){ // update weights for w1
                for(int x = 0; x < w2[0].length; x++){
                    w2[j][x] -= learnRate * (a2[j] * l2[j]);
                }
            }
            
            //System.out.println("\nUpdating w3");
            for(int j = 0; j < w3.length; j++){ // update weights for w2
                for(int x = 0; x < w3[0].length; x++){
                    w3[j][x] -= learnRate * (a3[j] * l3[j]);
                    //System.out.println(w2[j][x]+" ");
                }
            }
            
            // compute error for all hidden layers
            // updates weights using heuristic optimization
            // w = w - (learning rate)*(a(l-1)* lambda(l))
            
            //reset z1, z2, a1, a2
            for(int j = 0; j < z1.length; j++){
                z1[j] = 0;
                a1[j] = 0;
            }
            for(int j = 0; j < z2.length; j++){
                z2[j] = 0;
                a2[j] = 0;
            }
            for(int j = 0; j < z3.length; j++){
                z3[j] = 0;
                a3[j] = 0;
            }
        }
    }
    
    public static double test(List<Joint> joint) throws IOException {
        double z1[] = new double[3]; // #hidden nodes
        double z2[] = new double[3]; // #hidden nodes
        double z3[] = new double[2]; // #final nodes
        
        double a1[] = new double[3]; // #hidden nodes
        double a2[] = new double[3]; // #hidden nodes
        double a3[] = new double[2]; // #final nodes      a2[0] = 1 if male    a2[1] = 1 if female
        
        double[] normInput = new double[128*120];
        int max, min;
        
        String dir = "";
        int numCorrect = 0;
        
        File [] files = new File [joint.size()];
        
        for (int a = 0; a < joint.size(); ++a){
            files[a] = (joint.get(a)).file;
        }
        
        int[] input = new int[128*120];
        

        
        for (int i = 0; i < files.length; ++i){ // for each training image
            //read in image array
            if ((joint.get(i)).result == 0){
                dir = "Male/";
            }
            else{
                dir = "Female/";
            }
            FileReader fr = new FileReader(dir+files[i].getName());
            Scanner s = new Scanner(fr);
            
            if (!files[i].getName().endsWith(".txt")) continue;
            max = 0;
            min = 1000;
            for (int j = 0; j < input.length; j++){
                input[j] = s.nextInt();
                if(input[j] < min)
                    min = input[j];
                if(input[j] > max)
                    max = input[j];
            }
            
            //normalize data
            for(int j =  0; j < input.length; j++){
                normInput[j] = (input[j] - min)/(max - min);
            }

            
            
            //compute z1
            for(int j = 0; j < w1.length; j++){
                for(int x = 0; x < w1[0].length - 1; x++){
                    z1[j] += w1[j][x]  * normInput[x];
                }
            }
            
            
            // compute a(l) for all layers
            for (int j = 0; j < z1.length; j++){
                a1[j] = sigmoid(z1[j]); // apply sigmoid function to z1
            }
            
            //compute z2
            for(int j = 0; j < w2.length; j++){
                for(int x = 0; x <w2[0].length; x++){
                    z2[j] += w2[j][x] * a1[x];
                }
            }
            
            //compute a2
            for(int j = 0; j < z2.length; j++){
                a2[j] = sigmoid(z2[j]);
            }
            
            //compute z3
            for(int j = 0; j < w3.length; j++){
                for(int x = 0; x < w3[0].length; x++){
                    z3[j] += w3[j][x] * a2[x];  // uses weights in second layer to produce z2
                }
            }
            
            for (int k = 0; k < z3.length; ++k){
                a3[k] = sigmoid(z3[k]);
            }
            
            //results
            if(a3[0] <= a3[1]){ //predicts male
                if (dir == "Male/"){
                    numCorrect++;
                }
            }
            else{//predicts female
                if (dir == "Female/"){
                    numCorrect++;
                }
            }
            
            
            //reset z1, z2, a1, a2
            for(int j = 0; j < z1.length; j++){
                z1[j] = 0;
                a1[j] = 0;
            }
            for(int j = 0; j < z2.length; j++){
                z2[j] = 0;
                a2[j] = 0;
            }
            for(int j = 0; j < z3.length; j++){
                z3[j] = 0;
                a3[j] = 0;
            }
            
        }//end training loop
        
        //System.out.println("Correct predictions: "+ numCorrect +" out of "+ files.length);
        double percentage = (numCorrect*1.0) / (files.length*1.0);
        //System.out.println("percentage is: "+percentage);
        
        return percentage;
    }
    
    
    public static void test(String dir, File[] files) throws IOException {
        double z1[] = new double[3]; // #hidden nodes
        double z2[] = new double[3]; // #hidden nodes
        double z3[] = new double[2]; // #final nodes
        
        double a1[] = new double[3]; // #hidden nodes
        double a2[] = new double[3]; // #hidden nodes
        double a3[] = new double[2]; // #final nodes      a2[0] = 1 if male    a2[1] = 1 if female
        
        double[] normInput = new double[128*120];
        int max, min;
        
        int[] input = new int[128*120];
        
        double CI = 0;
        
        System.out.println("--PREDICTIONS--");
        
        for (int i = 0; i < files.length; ++i){ // for each training image
            //read in image array=
            if (!files[i].getName().endsWith(".txt")) continue;
            FileReader fr = new FileReader(dir+"/"+files[i].getName());
            Scanner s = new Scanner(fr);
            
            max = 0;
            min = 1000;
            for (int j = 0; j < input.length; j++){
                input[j] = s.nextInt();
                if(input[j] < min)
                    min = input[j];
                if(input[j] > max)
                    max = input[j];
            }
            
            //normalize data
            for(int j =  0; j < input.length; j++){
                normInput[j] = (input[j] - min)/(max - min);
            }
            
            //compute z1
            for(int j = 0; j < w1.length; j++){
                for(int x = 0; x < w1[0].length - 1; x++){
                    z1[j] += w1[j][x]  * normInput[x];
                }
            }
            
            // compute a(l) for all layers
            for (int j = 0; j < z1.length; j++){
                a1[j] = sigmoid(z1[j]); // apply sigmoid function to z1
            }
            
            //compute z2
            for(int j = 0; j < w2.length; j++){
                for(int x = 0; x <w2[0].length; x++){
                    z2[j] += w2[j][x] * a1[x];
                }
            }
            
            //compute a2
            for(int j = 0; j < z2.length; j++){
                a2[j] = sigmoid(z2[j]);
            }
            
            //compute z3
            for(int j = 0; j < w3.length; j++){
                for(int x = 0; x < w3[0].length; x++){
                    z3[j] += w3[j][x] * a2[x];  // uses weights in second layer to produce z2
                }
            }
            
            for (int k = 0; k < z3.length; ++k){
                a3[k] = sigmoid(z3[k]);
            }
            
            //results
            if(a3[0] >= a3[1]){ //predicts male
                System.out.println("MALE");
                CI += CI(a3[1], a3[0]);
            }
            else{//predicts female
                System.out.println("FEMALE");
                CI += CI(a3[0], a3[1]);
            }
            
            
            //reset z1, z2, a1, a2
            for(int j = 0; j < z1.length; j++){
                z1[j] = 0;
                a1[j] = 0;
            }
            for(int j = 0; j < z2.length; j++){
                z2[j] = 0;
                a2[j] = 0;
            }
            for(int j = 0; j < z3.length; j++){
                z3[j] = 0;
                a3[j] = 0;
            }
            
        }//end training loop
        
        System.out.println("\nConfidence Interval: "+(CI/files.length));
    }

    
    public static double CI (double a, double b) {
        return Math.pow(a/(a+b), .5)*100;
    }
    
    public static double sigmoid (double z) { // ACTIVATION FUNCTION
        return 1/(1 + Math.pow(2.7182818285, -z));
    }
}
