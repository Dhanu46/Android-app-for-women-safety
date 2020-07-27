package com.sdm.savior.model;

import java.util.ArrayList;

public class ContactsList
{
    private static ArrayList<Contact> listOfContacts = new ArrayList();

    public static void addContact(Contact contact)
    {
        listOfContacts.add(contact);
    }

    public static void deleteContact(Contact contact)
    {
        listOfContacts.remove(contact);
    }

    public static void setContacts(ArrayList<Contact> contacts)
    {
        listOfContacts.clear();
        listOfContacts.addAll(contacts);
    }

    public static ArrayList<Contact> getContacts()
    {
        return listOfContacts;
    }

    public static void clearContacts()
    {
        listOfContacts.clear();
    }

    public static boolean containsItem(Contact contact)
    {
        return listOfContacts.contains(contact);
    }

    public static int count()
    {
        return listOfContacts.size();
    }

    public static Contact getContactAtIndex(int index)
    {
        return listOfContacts.get(index);
    }

    public static void deleteContactAtIndex(int index)
    {
         listOfContacts.remove(index);
    }

}
