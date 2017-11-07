'''
Created on Mar 20, 2017

@author: priyaa
'''

#Importing all the functions to main

from ExtractTrain import training, output_list
from ExtractTrain_WithoutStopWord import training_stop
from ExtractTest import test
from ExtractTest_WithoutStopWord import test_stop1
from LogisticRegression import formequation
from NaiveBayes import naivebayes_accuracy
from ExtractTrain import stop_words
from LogisticRegression import calculateaccuracy
import sys

'''

# Need to have acces to sys.stdout
fd = open('output.txt','w') # open the result file in write mode
old_stdout = sys.stdout   # store the default system handler to be able to restore it
sys.stdout = fd # Now your file is used by print as destination

'''
#Get the command line arguments

training_ham_folder=sys.argv[1]
training_spam_folder=sys.argv[2]
test_ham_folder=sys.argv[3]
test_spam_folder=sys.argv[4]
Learning_Rate=float(sys.argv[5])
Lamda=float(sys.argv[6])
stop_words_txt=sys.argv[7]

'''

training_ham_folder="C:/Users/Priyaa/Desktop/Machine Learning/ML_Assignment2/2/hw2_train/train/ham"
training_spam_folder="C:/Users/Priyaa/Desktop/Machine Learning/ML_Assignment2/2/hw2_train/train/ham"
test_ham_folder="C:/Users/Priyaa/Desktop/Machine Learning/ML_Assignment2/2/hw2_test/test/ham"
test_spam_folder="C:/Users/Priyaa/Desktop/Machine Learning/ML_Assignment2/2/hw2_test/test/spam"
Learning_Rate=0.01
Lamda=-0.001
stop_words_file="C:/Users/Priyaa/Desktop/Machine Learning/ML_Assignment2/STOP_WORDS.txt"

'''

print('************','Running Naive Bayes with out stop words', '***************')

weightvector,inputvector,positive,negative,negative_attribute_count,positive_attribute_count,wordlist_train,wordcount,train_output_list=training(training_ham_folder,training_spam_folder)
wordlist_test,wordpositions,output_list,test_attributelist=test(wordlist_train,test_ham_folder,test_spam_folder)


accuracy=naivebayes_accuracy(positive, negative, negative_attribute_count, positive_attribute_count, wordlist_train, wordcount, wordlist_test, wordpositions, output_list)


print('************','Running Logistic Regression with out stop words', '************')

weight_matrix=formequation(inputvector, weightvector, train_output_list,0,15,Learning_Rate,Lamda)
logistic_accuracy=calculateaccuracy(weight_matrix,test_attributelist,output_list)


print('************','Running Naive Bayes with stop words','***********')

stop_word=stop_words(stop_words_txt)
stop_weightvector,stop_inputvector,stop_positive,stop_negative,stop_negative_attribute_count,stop_positive_attribute_count,stop_wordlist,stop_wordcount,stop_train_output_list=training_stop(stop_word,training_ham_folder,training_spam_folder)
test_wordlist,test_wordpositions,stop_output_list,stop_test_attributelist=test_stop1(stop_word,stop_wordlist,test_ham_folder,test_spam_folder)

accuracy_stop=naivebayes_accuracy(stop_positive,stop_negative,stop_negative_attribute_count,stop_positive_attribute_count,stop_wordlist,stop_wordcount,test_wordlist,test_wordpositions,stop_output_list)



print('***********','Running Logistic Regression with stop words','**************')

weight_matrix_stop=formequation(stop_inputvector, stop_weightvector, stop_train_output_list,0,5,Learning_Rate,Lamda)
logistic_accuracy_stop=calculateaccuracy(weight_matrix_stop,stop_test_attributelist,stop_output_list)


print("******  Accuracy   *******")
print("\nNaive Bayes:", accuracy*100)
print("\nLogistic Regression:",logistic_accuracy*100)
print("\nNaive Bayes with stop words:",accuracy_stop*100)
print("\nLogistic Regression with stop words:",logistic_accuracy_stop*100)

