'''


*********Logistic Regression---Accuracy*************


'''
import math
from ctypes.wintypes import DOUBLE


def formequation(inputvector, weightvector, outputvector, check, total, Learning_Rate, Lamda):
    '''Weight matrix updation as per the formula'''
    summation = 0
    i = 0
    j = 0
    w0 = 0
    gradient_ascent = []

    weightmatrix = weightvector[0]
    for each_row in inputvector:
        number_of_paramerts = each_row
        for parameters in each_row:
            # numerator term of the formula
            summation = (inputvector[i][j] * weightmatrix[j]) + summation
            j = j + 1
        try:
            # denominator term of the formula
            denominator = float(math.exp(w0 + summation))
        except:
            denominator = w0 + summation

        #adding 1 as in formula
        den = denominator + 1
        output_value = 1
        if outputvector[i] == 'TRUE':
            output_value = 1
        else:
            output_value = 0
        #add the diff value in the lsit to keep track
        gradient_ascent.insert(i, output_value - (denominator / den))
        i = i + 1
        j = 0

    i = 0
    j = 0
    sum = 0


    for each_paramaters in number_of_paramerts:

        for eachrow in inputvector:
            sum = sum + ((inputvector[j][i]) * gradient_ascent[j])
            j = j + 1
        weightmatrix[i] = weightmatrix[i] + (Learning_Rate * sum) - (Learning_Rate * Lamda * weightmatrix[i])
        i = i + 1
        j = 0
    # print(weightvector)
    #repeat until convergence-----check ---no of iterations
    if check <= total:
        formequation(inputvector, weightvector, outputvector, check + 1, total, Learning_Rate, Lamda)
    return weightmatrix


def calculateaccuracy(weight_matrix, test_input, test_output):
    '''Check for accuracy'''
    i = 0
    j = 0
    classifyPositive = 0
    classifyNegative = 0
    for each_row in test_input:
        sum = 0
        for each_col in each_row:
            # print(i,j)
            sum = sum + (weight_matrix[j] * test_input[i][j])
            j = j + 1
        if (sum > 0.5):
            value = "TRUE"
        else:
            value = "FALSE"
        '''Compare  the result I got with the actual true classification'''
        if (value == test_output[i]):
            classifyPositive = classifyPositive + 1
        else:
            classifyNegative = classifyNegative + 1
        i = i + 1
        j = 0
    #print('\nclass positive', class_positive)
    #print('\nclass neg', class_negative)
    '''Return the accuracy---how much percent it classifies the test data correctly'''
    return classifyPositive / (classifyNegative + classifyPositive)
