from tabulate import tabulate
from tkinter import *
from tkinter import messagebox
from tkinter import scrolledtext
vocab = {'А':'A', 'Б':'В', 'В':'W', 'Г':'G',
'Д':'D', 'Е':'E','Ё':'ch','Ж':'J', 'З':'Z', 'И':'I',
'Й':'and', 'К':'K', 'Л':'L','М':'M','Н':'N','О':'O',
'П':'P', 'Р':'R','С': 'S', 'Т': 'T','У':'U', 'Ф':'F', 
'Х':'H','Ц':'C','Ч':'Q','Ш':'wh','Щ':'X', 'Ъ':'of', 'Ы':'the',
'Ь':'with','Э':'ow','Ю':'ou','Я':'ed'}


def Table():
    return tabulate(vocab.items(), headers=['Russian braille', 'English braille'],tablefmt="fancy_grid")


def Convert(letters):
    converted = []
    letters = letters.upper()
    for i in range(len(letters)):
        if letters[i] != ' ':
            converted.append(vocab.get(letters[i]))
        else:
            converted.append(' ')
        if converted != []:
            if converted[i] == None and letters[i] != ' ':
                messagebox.showerror("Input mistake", "Object "+ letters[i] +" can't be converted")
                return ""
    return ''.join(converted)

def Print(converted):
    messagebox.showinfo("English braille", ''.join(converted))
    

def display():
    console.configure(state='normal')  
    console.insert(END, Convert(message_entry.get()) + '\n')
    console.yview(END)  
    console.configure(state='disabled')  
         
if __name__ == "__main__":
    root = Tk()
    root.title("Russian braille conventer")
    root.geometry("400x300")

    message_entry = Entry(root)
    message_entry.place(relx=.5, rely=.1, anchor="c")
    message_entry.pack()
    message_button = Button(text="convert", command=display)
    message_button.place(relx=.5, rely=.2, anchor="c")
    message_button.pack()

    console = scrolledtext.ScrolledText(root, state='disable')
    console.configure(state='normal')  
    console.insert(END, Table() + '\n')
    console.yview(END)  
    console.configure(state='disabled')  
    console.pack()
    root.mainloop()