//The questions have been adapted from Lorraine Whitmarsh , (2003). Title:Survey of environmental concerns .  URL:https://canvas.sydney.edu.au/courses/20614/files/9883161/download?wrap=1



//if form is properly filled data will be stored to local storage,then form will be submitted
function checksavesend()
{
    
    if (checkallfilled())
    {
        if (checkallvalid())
        {
            storedata();
            window.location.href='summary.html'
        }
        
    }
}


//combines all necesary check functions, returns true if all required fields have been filled
function checkallfilled()
{
    //checks all drop boxes are filled,if all checks return true the if statement does not execute
    if (!(checkfilleddrop('climatechange?')&&checkfilleddrop('weatherchange?')&&checkfilleddrop('importance')&&checkfilleddrop('personaleffect')&&checkfilleddrop('responsibility')&&checkfilleddrop('takenaction?')))
    {
        alert("please select an option for all drop boxes.")
        return false
    }
    //checks all text input are filled, same concept as above check
    if (!(checkfilled('age')&&checkfilled('country')&&checkfilled('state')&&checkfilled('suburb')&&checkfilled('email')&&checkfilled('effects')&&checkfilled('causes')&&checkfilled('reason')&&checkfilled('strategy')))
    {
        alert("please fill in all required text answers.")
        return false
    }
    //checks the fields where the previous question must be answered yes for it to be required
    if (!(checkifyesfilled1()&&checkifyesfilled2()&&checkifyesfilled3()))
    {
        alert("please fill in all follow up questions which you have answered 'yes' to the corresponding questions.")
        return false
    }
    //checks radio type inputs are filled, note that the question about climate issues is intentionally left not required
    if (!(checkifchecked('gender')))
    {
        alert("please select an option for the gender field.")
        return false
    }
    return true
}
//checks if fields have been filled properly
function checkallvalid()
{
    if (!checkage())
    {
        alert("please enter a numeric age")
        return false
    }
    if (!check5limit())
    {
        alert("please only select upto five environmental issues")
        return false
    } 
    if (!checkemail())
    {
        alert("please use a valid email, e.g. john@gmail.com")
        return false
    } 
    return true
}
//checks if age is numeric, note that if the field is empty this function returns true but the function checkfilled() is called before this function so it does not matter
function checkage()
{
    if(isNaN(document.getElementById('age').value)) 
    {
        return false;
    }
    else
    {
        return true;
    }
}
//checks if 5 option limit has been passed
function check5limit()
{
    var nodelist =Array.from(document.getElementsByName('issues'));
    var is_checked =['','','','','','','','',''];
    //counts how many variables have been checked
    var count=0
    //makes an array of boolean values which show if each index is checked
    for(var i=0;i<nodelist.length;i++)
    {
        is_checked[i]=nodelist[i].checked
    }
    //adds 1 to the count variable for every true
    for(var i=0;i<nodelist.length;i++)
    {
        if (is_checked[i]==true)
        {
            count=count+1
        }
        
    }
    //checks if count is over the limit
    if (count>5)
    {
        return false
    }
    else
    {
        return true
    }
}
//checks if a dropdown menu of certain id has been answered with a 'yes' string
function checkemail()
{
    var email=document.getElementById('email').value
    if (email.includes('@'))
    {
        return true
    }
    else
    {
        return false
    }
}
//checks if yes was selected as an option
function checkifyes(x)
{
    var input=document.getElementById(x).value
    if (input=="yes")
    {
        return true
    }
    else
    {
        return false
    }

}
//checks if the string is 'no', working with local storage, intended for use on summary page
function checkifno(x)
{
    var input=localStorage.getItem(x)
    if (input=="no")
    {
        return true
    }
    else
    {
        return false
    }

}
//checks if a certain text input is filled
function checkfilled(x)
{
    var input=document.getElementById(x).value;
    if (input=="")
    {
        return false
    }
    else
    {
        return true
    }

}
//checks if a any option has been selected for a dropdown menu
function checkfilleddrop(x)
{
    var input=document.getElementById(x).value;
    if (input=="")
    {
        return false
    }
    else
    {
        return true
    }
}
//checks if a a radio or checkbox has been checked
function checkifchecked(x)
{
    var nodelist =Array.from(document.getElementsByName(x));
    //checks if object has been checked
    for(var i=0;i<nodelist.length;i++)
    {
        if (nodelist[i].checked)
        {
            return true
        }
    }
    //returns a false if nothing is checked
    return false

}
//if the previous question is answered with a yes, this function checks if the corresponding question has been filled
function checkifyesfilled1()
{
    if (checkifyes("climatechange?"))
    {
        if (!checkifchecked('medium'))
        {
            if (!checkfilled('medium6'))
            {
                return false
            }
        }
    }
    return true
}
//similar to previous function but for diffferent set of questions
function checkifyesfilled2()
{
    if (checkifyes("weatherchange?"))
    {
        if (!checkfilled('weatherchangeexplain'))
        {
            return false
        }
    }
    return true
}
//similar to previous function but for different set of questions
function checkifyesfilled3()
{
    if (checkifyes("personaleffect"))
    {
        if (!checkfilled('whateffect'))
        {
            return false
        }
    }
    return true
}
//outputs an array of all checked/ entered fields from a nodelist
function returninfo(x)
{
    var nodelist =(document.getElementsByName(x));
    output=[]
    for(var i=0;i<nodelist.length;i++)
    {
        if (nodelist[i].checked==true)
        {
            output.push(nodelist[i].value)
        }
        if (nodelist[i].type=='text')
        {
            if (nodelist[i].value!='')
            {
                output.push(nodelist[i].value)
            }
        }
    }
    return output
    

}
//returns string of each array element spaced out by ', '
function arraycombine(x)
{
    output=''
    for(var i=0;i<x.length;i++)
    {
        output=output+x[i]+', '
    }
    //cuts off off end comma and space
    return output.slice(0,-2)
}
//uses name to to convert all entered fields to a single line sting
function nodelist_to_string(x)
{
    return arraycombine(returninfo(x))
}
//stores all fields to local storage
function storedata()
{
    localStorage.setItem('age',document.getElementById('age').value)
    localStorage.setItem('gender',nodelist_to_string('gender'))
    localStorage.setItem('residence',nodelist_to_string('residence'))
    localStorage.setItem('email',document.getElementById('email').value)
    localStorage.setItem('climatechange?',document.getElementById('climatechange?').value)
    localStorage.setItem('medium',nodelist_to_string('medium'))
    localStorage.setItem('weatherchange?',document.getElementById('weatherchange?').value)
    localStorage.setItem('weatherchangeexplain',document.getElementById('weatherchangeexplain').value)
    localStorage.setItem('issues',nodelist_to_string('issues'))
    localStorage.setItem('effects',document.getElementById('effects').value)
    localStorage.setItem('causes',document.getElementById('causes').value)
    localStorage.setItem('importance',document.getElementById('importance').value)
    localStorage.setItem('reason',document.getElementById('reason').value)
    localStorage.setItem('personaleffect',document.getElementById('personaleffect').value)
    localStorage.setItem('whateffect',document.getElementById('whateffect').value)
    localStorage.setItem('responsibility',document.getElementById('responsibility').value)
    localStorage.setItem('takenaction?',document.getElementById('takenaction?').value)
    localStorage.setItem('strategy',document.getElementById('strategy').value)
    
}
//function takes key to a local storage item (x), and the id (y) of a certain field in the summarry
//creates a <strong> tag and places textual info into it, gives it a class for easy CSS application and appends child to a certain node by id
//note that the tag type is not important, it is just used to attach a class to the text
function display(x,y)
{
    var temp = document.createElement('stron')
    var text = document.createTextNode(localStorage.getItem(x))
    temp.setAttribute('class','answer')
    temp.appendChild(text)
    document.getElementById(y).appendChild(temp)

}



