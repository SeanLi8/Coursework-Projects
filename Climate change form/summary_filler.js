
//display all info
display('age','field_1')
display('gender','field_2')
display('residence','field_3')
display('email','field_4')
display('climatechange?','field_5')
display('medium','optional_1')
display('weatherchange?','field_6')
display('weatherchangeexplain','optional_2')
display('issues','field_7')
display('effects','field_8')
display('causes','field_9')
display('importance','field_10')
display('reason','field_11')
display('personaleffect','field_12')
display('whateffect','optional_3')
display('responsibility','field_13')
display('takenaction?','field_14')
display('strategy','field_15')

//removes fields that are not entered, (the follow up questions)
if (checkifno('climatechange?'))
{
    document.getElementById('optional_1').remove()
}
if (checkifno('weatherchange?'))
{
    document.getElementById('optional_2').remove()
}
if (checkifno('personaleffect'))
{
    document.getElementById('optional_3').remove()
}

