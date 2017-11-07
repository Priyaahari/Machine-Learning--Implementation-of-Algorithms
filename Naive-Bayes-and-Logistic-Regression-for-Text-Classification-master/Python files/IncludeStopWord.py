'''

****Parse the stop words text file ***********

'''
'''
from ExtractTrain import wordlist

def stop_words(stop_wrords_file):
    stop_words_list=[]
    words_list=[]
    with open(stop_wrords_file,'r') as my_file:
                try:
                    s=my_file.read()
                    words_list=s.split()
                    #print(words_list)
                    for each_word in wordlist:
                        stop_words_list.append(each_word)
                except:
                    pass
    return words_list
'''